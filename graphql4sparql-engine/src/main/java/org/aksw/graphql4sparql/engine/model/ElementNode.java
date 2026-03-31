package org.aksw.graphql4sparql.engine.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.aksw.graphql4sparql.engine.api2.Connective;
import org.aksw.graphql4sparql.engine.api2.ConnectiveVisitor;
import org.aksw.graphql4sparql.engine.api2.ConnectiveVisitorToString;
import org.aksw.graphql4sparql.engine.api2.ElementTransform;
import org.aksw.graphql4sparql.engine.api2.HasParentVarBuilder;
import org.aksw.graphql4sparql.engine.api2.HasTargetVarBuilder;
import org.aksw.graphql4sparql.engine.api2.Selection;
import org.aksw.graphql4sparql.engine.api2.SparqlPathTraversable;
import org.aksw.graphql4sparql.engine.util.StringUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.core.VarExprList;
import org.apache.jena.sparql.path.P_Path0;
import org.apache.jena.sparql.path.Path;

/**
  * The attributes of the node are static, but the children are mutable.
  */
public class ElementNode
//    extends BasicConnectInfo
//    extends SelectionSet<Selection>
    implements Selection, SparqlPathTraversable<ElementNode>
{
    /**
     * A link to the parent node.
     *
     * @param parent The parent element node
     * @param name The name of this node in the parent's children
     */
    public record ParentLink(ElementNode parent, String name) {
        /**
         * Avoid cycles in toString().
         *
         * @return The string representation
         */
        @Override
        public final String toString() {
            return "ParentLink[" + name + "]";
        }
    }

    /**
     * Which variables of the parent join with which variables of the child.
     *  Could extend to List&gt;Expr&gt; or ExprList.
     *
     * @param child The child element node
     * @param parentVars The parent variables
     * @param childVars The child variables
     */
  public record JoinLink(ElementNode child, List<Var> parentVars, List<Var> childVars) {
        /**
         * Creates a new JoinLink.
         *
         * @param child The child element node
         * @param parentVars The parent variables
         * @param childVars The child variables
         */
        public JoinLink {
            if (parentVars.size() != childVars.size()) {
                throw new IllegalArgumentException("Variable lists must have equal size. Got: " + parentVars + ", " + childVars);
            }
        }

        /**
         * Returns the number of variable pairs in this join link.
         *
         * @return The size of the parent variables list
         */
        public int size() {
            return parentVars.size();
        }

        /**
         * Avoid cycles in toString().
         *
         * @return The string representation
         */
        @Override
        public final String toString() {
            return "JoinLink[parent: " + parentVars + ", childVars: " + childVars + "]";
        }
    }

    /* parent / child management */

    /**
     * The link to the parent node.
     */
    protected ParentLink parentLink;

    /**
     * The map of child links by name.
     */
    protected Map<String, JoinLink> nameToChildLink;

    /* Core SPARQL-related fields */

    /**
     * A label that serves as a basis for allocating names when a node becomes a child of another. May be null.
     */
    protected String label;

    /**
     * Connective: graph pattern with default source and target vars.
     *  The defaults can be overridden.
     */
    protected Connective connective;

    // Declared variable references
    // TODO Object

    /* Extra information */

    /**
     * The offset for pagination.
     */
    protected Long offset;

    /**
     * The limit for pagination.
     */
    protected Long limit;

    /**
     * An RDF term that identifies this node. Should be suitable for use as a discriminator value when generating queries.
     */
    protected String identifier;

    /**
     * List of local transformations.
     */
    protected List<ElementTransform> localTransforms = new ArrayList<>();

    /**
     * List of transformations that apply to the whole subtree of the assembled query.
     *  For example, wrapping them in a GRAPH or SERVICE block.
     *  Transformers should declare any variables they introduce and they must not invalidate
     *  existing ones.
     */
    protected List<ElementTransform> treeTransforms = new ArrayList<>();

    /**
     * Bind expressions, can reference variables in any ancestor.
     */
    protected VarExprList binds = new VarExprList();

    // The target vars, may originate from the connective and/or the bind expressions
    // (and the ancestores)
    /**
     * Local target variables.
     */
    protected List<Var> localTargetVars;

    /**
     * Creates a new ElementNode.
     *
     * @param label The label for this node
     * @param connective The connective for this node
     */
    protected ElementNode(String label, Connective connective) {
        super();
        this.label = label;
        this.connective = Objects.requireNonNull(connective);
        this.nameToChildLink = new LinkedHashMap<>();
    }

  /**
     * Returns the local target variables.
     *
     * @return The local target variables
     */
    public List<Var> getLocalTargetVars() {
        return localTargetVars;
    }

    /**
     * Sets the local target variables.
     *
     * @param localTargetVars The local target variables to set
     * @return This ElementNode
     */
    public ElementNode setLocalTargetVars(List<Var> localTargetVars) {
        if (nameToChildLink.size() > 0) {
            throw new IllegalStateException("Cannot set/update target variables once children have been added.");
        }

        this.localTargetVars = localTargetVars;
        return this;
    }

    /**
     * Returns the effective target variables.
     *
     * @return The effective target variables
     */
    public List<Var> getEffectiveTargetVars() {
        List<Var> result = localTargetVars != null ? localTargetVars : connective.getDefaultTargetVars();
        return result;
    }

    /**
     * Returns the parent link.
     *
     * @return The parent link, or null if this is the root node
     */
    public ParentLink getParentLink() {
        return parentLink;
    }

    /**
     * Starting from this node and moving up its ancestors, find the first node which declares var.
     *  Returns null if there is no match.
     *
     * @param var The variable to find
     * @return The element node that declares the variable, or null if not found
     */
    public ElementNode findVarInAncestors(Var var) {
        ElementNode result = connective.getVisibleVars().contains(var) || binds.getVars().contains(var)
            ? this
            : parentLink == null
                ? null
                : parentLink.parent.findVarInAncestors(var);
        return result;
    }

    @Deprecated // Hack for compatibility
    /**
     * Returns the selections of this node.
     *
     * @return The selections
     */
    public List<Selection> getSelections() {
        return nameToChildLink.values().stream().map(link -> (Selection)link.child()).toList();
    }

 /**
     * Returns the label of this node.
     *
     * @return The label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns whether this is the root node.
     *
     * @return True if root, false otherwise
     */
    public boolean isRoot() {
        return parentLink == null;
    }

    /**
     * Sets the label of this node.
     *
     * @param label The label to set
     * @return This ElementNode
     */
    public ElementNode setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Get the name by which this node is referenced by the parent. null for the root node.
     *
     * @return The assigned name
     */
    public String getAssignedName() {
        String result = parentLink == null
            ? null
            : parentLink.name();
        return result;
    }

    @Override
    public String getName() {
        String result = getAssignedName();
        if (result == null) {
             result = "root";
        }
        return result;
    }

    /**
     * Returns the connective for this node.
     *
     * @return The connective
     */
    public Connective getConnective() {
        return connective;
    }

    /**
     * Get link by which this node's variables joins to those of the parent.
     *  null if there is no parent.
     *
     * @return The join link, or null if there is no parent
     */
    public JoinLink getJoinLink() {
//        JoinLink result = parentLink == null
//            ? null
//            : parentLink.parent().getChildrenByName().get(parentLink.name());
        JoinLink result = null;
        if (parentLink != null) {
            Map<String, JoinLink> map = parentLink.parent().getChildrenByName();
            String lookupName = parentLink.name();
            result = map.get(lookupName);
        }
        return result;
    }

/**
     * Returns the identifier of this node.
     *
     * @return The identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * The identifier is a string to that it can be used both as a prefix for variables as well
     *  as string literals in a result set.
     *
     * @param identifier The identifier to set
     * @return This ElementNode
     */
    public ElementNode setIdentifier(String identifier) {
        // return setIdentifier(NodeFactory.createLiteral(identifier));
        this.identifier = identifier;
        return this;
    }

    /**
     * Returns the bind expressions.
     *
     * @return The bind expressions
     */
    public VarExprList getBinds() {
        return binds;
    }

//    public ElementNode setIdentifier(Node identifier) {
//        this.identifier = identifier;
//        return this;
//    }

    @Override
    public <T> T accept(ConnectiveVisitor<T> visitor) {
        T result = visitor.visit(this);
        return result;
    }

    @Override
    public String toString() {
        String result = ConnectiveVisitorToString.toString(this);
        return result;
    }

    /**
     * Returns the map of children by name.
     *
     * @return The children map
     */
    public Map<String, JoinLink> getChildrenByName() {
        return nameToChildLink;
    }

    /** Return a new builder for a root field. */
//    public static ElementNodeBuilder newBuilder() {
//        return new ElementNodeBuilder();
//    }

    /**
     * Convenience function to produce a new ElementNode that is immediately attached to this node.
     *
     * @param path The path to step with
     * @return The new ElementNode
     */
    @Override
    public ElementNode step(Path path) {
        Objects.requireNonNull(path);

        String baseName = null;
        if (path instanceof P_Path0 p0) {
            Node node = p0.getNode();
            if (node.isURI()) {
                baseName = node.getLocalName();
            }
        }

        Connective connective = Connective.of(path);
        ElementNode result = ElementNode.of(baseName, connective);
        addChild(result);
        return result;

    }

    /**
     * Adds a child node using default variable bindings.
     *
     * @param child The child node to add
     * @return This ElementNode
     */
    public ElementNode addChild(ElementNode child) {
        return addChild(connective.getDefaultTargetVars(), child, child.getConnective().getConnectVars());
    }

    /**
     * Prepares a child node for addition.
     *
     * @return A new ChildAdder
     */
    public ChildAdder prepareChild() {
        return new ChildAdder(this);
    }

    /**
     * Adds a child node with the given variables.
     *
     * @param thisVars The variables of this node to join with
     * @param child The child node to add
     * @return This ElementNode
     */
    public ElementNode addChild(List<Var> thisVars, ElementNode child) {
        return addChild(thisVars, child, child.getConnective().getConnectVars());
    }

    /**
     * Sets the limit for pagination.
     *
     * @param limit The limit to set
     * @return This ElementNode
     */
    public ElementNode setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Returns the limit for pagination.
     *
     * @return The limit
     */
    public Long getLimit() {
        return limit;
    }

    /**
     * Sets the offset for pagination.
     *
     * @param offset The offset to set
     * @return This ElementNode
     */
    public ElementNode setOffset(Long offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Returns the offset for pagination.
     *
     * @return The offset
     */
    public Long getOffset() {
        return offset;
    }

    /**
     * Add a child with the given join condition.
     *  Returns this (not the child)
     *
     * @param thisVars The variables of this to join with
     * @param child The child node to add
     * @param childVars The child variables to join with
     * @return This ElementNode
     */
    public ElementNode addChild(List<Var> thisVars, ElementNode child, List<Var> childVars) {
        Objects.requireNonNull(child);

        List<Var> finalThisVars = thisVars;
        List<Var> finalChildVars = childVars;

        if (child == this) {
            throw new IllegalArgumentException("Attempt to add a node to itself");
        }

        if (finalChildVars == null) {
            finalChildVars = child.getConnective().getConnectVars();
        }

        if (finalThisVars == null) {
            // If childVars is empty and thisVars is unspecified (null) then
            // "join" with an empty list of variables
            finalThisVars = finalChildVars != null && finalChildVars.isEmpty()
                ? List.of()
                : getEffectiveTargetVars(); // connective.getDefaultTargetVars();
        }

        // Sanity checks
        if (finalThisVars.size() != finalChildVars.size()) {
            throw new RuntimeException(String.format("Join var lists differ in size: thisVars=%s, childVars=%s", finalThisVars, finalChildVars));
        }

        // Parent vars can be in any ancestor
        // Set<Var> absentParentVars = thisVars.stream().filter(x -> !connective.getVisibleVars().contains(x)).collect(Collectors.toSet());
        Set<Var> absentParentVars = finalThisVars.stream().filter(x -> findVarInAncestors(x) == null).collect(Collectors.toSet());
        if (!absentParentVars.isEmpty()) {
            throw new RuntimeException("Join variables do not exist on parent:" + finalThisVars);
        }

        Set<Var> absentChildVars = finalChildVars.stream().filter(x -> !child.getConnective().getVisibleVars().contains(x)).collect(Collectors.toSet());
        if (!absentChildVars.isEmpty()) {
            throw new RuntimeException("Join variables do not exist on child:" + finalThisVars);
        }

        String finalName = adaptName(this, child.getLabel());
        addChildInternal(finalName, finalThisVars, child, finalChildVars);

        return this;
    }

    /**
     * Adds a child node internally.
     *
     * @param finalName The final name
     * @param thisVars The this variables
     * @param child The child node
     * @param childVars The child variables
     */
    protected void addChildInternal(String finalName, List<Var> thisVars, ElementNode child, List<Var> childVars) {
        JoinLink link = new JoinLink(child, thisVars, childVars);

        // After adapt name there should not be a child with that name - state may corrupt in multithreaded environments
        // XXX Could be an assertion
        if (getChildrenByName().containsKey(finalName)) {
            throw new IllegalStateException();
        }

        getChildrenByName().put(finalName, link);
        child.setParent(new ParentLink(this, finalName));
    }

    void unlinkFromParent() {
        if (parentLink != null) {
            parentLink.parent().removeChildByName(parentLink.name());
            parentLink = null;
        }
    }

    // Internal
    void removeChildByName(String name) {
        nameToChildLink.remove(name);
    }

    // Internal
    void setParent(ParentLink parentLink) {
        unlinkFromParent();
        this.parentLink = parentLink;
    }

//    /** Add a child, join the child's default source vars with this node's default target vars */
//    public ElementNode addChild(ElementNode child) {
//        if (child instanceof ElementNode c) {
//            List<Var> parentVars = c.getParentVars();
//
//            List<Var> finalParentVars = parentVars != null ? parentVars : connective.getDefaultTargetVars(); // target.getDefaultTargetVars();
//            // Validation.validateParentVars(target, finalParentVars);
//
//            if (finalParentVars == null) {
//                throw new RuntimeException("Parent variables not set.");
//            }
//
//            if (finalParentVars.isEmpty()) {
//                throw new RuntimeException("Parent variable set is empty.");
//            }
//
//            Set<Var> absentParentVars = finalParentVars.stream().filter(x -> !connective.getVisibleVars().contains(x)).collect(Collectors.toSet());
//            if (!absentParentVars.isEmpty()) {
//                throw new RuntimeException("Cannot connect to the following variables which do not exist in the parent: " + absentParentVars);
//            }
//
//            List<Var> connectVars = connective.getConnectVars();
//            if (finalParentVars.size() != connectVars.size()) {
//                throw new RuntimeException("parentVars.length != connectVars.length: Cannot connect parent." + finalParentVars + " to this." + connective.getConnectVars());
//            }
//        }
//
//        selections.put(child.getName(), child);
//        return this;
//    }

    /**
     * Returns the local transformations.
     *
     * @return The local transformations
     */
    public List<ElementTransform> getLocalTransforms() {
        return localTransforms;
    }

    /**
     * Returns the tree transformations.
     *
     * @return The tree transformations
     */
    public List<ElementTransform> getTreeTransforms() {
        return treeTransforms;
    }

    /**
     * Adds a local transformation.
     *
     * @param elementTransform The transformation to add
     * @return This ElementNode
     */
    public ElementNode addLocalTransform(ElementTransform elementTransform) {
        this.localTransforms.add(elementTransform);
        return this;
    }

    /**
     * Adds a tree transformation.
     *
     * @param elementTransform The transformation to add
     * @return This ElementNode
     */
    public ElementNode addTreeTransform(ElementTransform elementTransform) {
        this.treeTransforms.add(elementTransform);
        return this;
    }

    /**
     * Adapts the name for a child node to avoid conflicts.
     *
     * @param parent The parent element node
     * @param label The proposed label
     * @return The adapted name
     */
    public static String adaptName(ElementNode parent, String label) {
        Set<String> childNames = parent.getChildrenByName().keySet();
        String finalBaseName = label == null ? "field" + (childNames.size() + 1) : label;
        String finalName = StringUtils.allocateName(finalBaseName, false, childNames::contains);
        return finalName;
    }


    /**
     * Creates a new ElementNode with the given connective.
     *
     * @param connective The connective for the node
     * @return A new ElementNode with null label
     */
    public static ElementNode of(Connective connective) {
        return of(null, connective);
    }

    /**
     * Creates a new ElementNode with the given label and connective.
     *
     * @param label The label for the node
     * @param connective The connective for the node
     * @return A new ElementNode
     */
    public static ElementNode of(String label, Connective connective) {
        return new ElementNode(label, connective);
    }


    /** Adjusts the name in the builder */
//    public static void adaptName(ElementNode parent, ElementNodeBuilder childBuilder) {
//        String name = childBuilder.getName();
//        String finalBaseName = name == null ? "field" : name;
//        Collection<String> childNames = parent.getChildrenByName().keySet();
//        String finalName = StringUtils.allocateName(finalBaseName + childNames.size(), true, childNames::contains);
//        childBuilder.name(finalName);
//    }
//
//    public static ElementNode attach(ElementNode parent, ElementNodeBuilder childBuilder) {
//        adaptName(parent, childBuilder);
//        ElementNode result = childBuilder.build();
//        parent.addChild(result);
//        return result;
//    }

    /**
     * Returns a clone of this element node.
     *
     * @return The cloned element node
     */
    @Override
    public ElementNode clone() {
        ElementNode result = new ElementNode(label, connective);
        result.offset = this.offset;
        result.limit = this.limit;
        result.identifier = this.identifier;
        result.localTransforms = new ArrayList<>(this.localTransforms);
        result.treeTransforms = new ArrayList<>(this.treeTransforms);
        for (Entry<String, JoinLink> childEntry : this.nameToChildLink.entrySet()) {
            JoinLink joinLink = childEntry.getValue();
            ElementNode newChild = childEntry.getValue().child.clone();
            addChildInternal(childEntry.getKey(), joinLink.childVars(), newChild, joinLink.parentVars());
        }
        return result;
    }

    /**
     * Helper class add child nodes.
     */
    public static class ChildAdder
        implements HasParentVarBuilder<ChildAdder>, HasTargetVarBuilder<ChildAdder>
    {
        /**
         * The parent node.
         */
        protected ElementNode parentNode;
        /**
         * The child node.
         */
        protected ElementNode childNode;
        /**
         * The parent variables.
         */
        protected List<Var> parentVars;
        /**
         * The child variables.
         */
        protected List<Var> childVars;


        /**
         * Creates a new child adder.
         *
         * @param parentNode The parent node
         */
        public ChildAdder(ElementNode parentNode) {
            super();
            this.parentNode = parentNode;
        }

        /**
         * Sets the child node.
         *
         * @param childNode The child node
         * @return This child adder
         */
        public ChildAdder child(ElementNode childNode) {
            this.childNode = childNode;
            return this;
        }


        /**
         * Sets the parent variables.
         *
         * @param vars The parent variables
         */
        @Override
        public void setParentVars(List<Var> vars) {
            this.parentVars = vars;
        }

        /**
         * Sets the target variables.
         *
         * @param vars The target variables
         */
        @Override
        public void setTargetVars(List<Var> vars) {
            this.childVars = vars;
        }

        /**
         * Attaches the child node to the parent.
         *
         * @return The child node
         */
        public ElementNode attach() {
            parentNode.addChild(parentVars, childNode, childVars);
            return childNode;
        }
    }
}
