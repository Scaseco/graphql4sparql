package org.aksw.graphql4sparql.engine.api2;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.aksw.graphql4sparql.engine.util.ElementUtils;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.graph.NodeTransform;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.PatternVars;

/**
 * A connective represents a connected graph pattern in the GraphQL query.
 */
public class Connective
    extends BasicConnectInfo
    implements ConnectiveNode
    // extends SelectionSet // Connective is not a SelectionSet - Field is.
{
    /**
     * The graph pattern.
     */
    protected final Element element;

    /**
     * The variables of the given element which to join on the parent variables.
     */
    protected final List<Var> connectVars;

    // Cached attributes
    /**
     * The algebra operator for this connective.
     */
    protected final Op op;

    /**
     * Creates a new Connective instance.
     *
     * @param element The graph pattern element
     * @param connectVars The variables to join on
     * @param defaultTargetVars The default target variables
     * @param op The algebra operator
     * @param visibleVars The set of visible variables
     */
    public Connective(Element element, List<Var> connectVars, List<Var> defaultTargetVars, Op op, Set<Var> visibleVars) {
        super(defaultTargetVars, visibleVars);
        this.element = element;
        this.connectVars = connectVars;
        this.op = op;
    }

    /**
     * Returns the graph pattern element.
     *
     * @return The graph pattern element
     */
    public Element getElement() {
        return element;
    }

    /**
     * Returns the variables to join on.
     *
     * @return The connect variables
     */
    public List<Var> getConnectVars() {
        return connectVars;
    }

    /**
     * Create a new connective (copy) where nodes have been remapped accordingly.
     *
     * @param nodeTransform The node transform to apply
     * @return A new connective with transformed nodes
     */
    public Connective applyNodeTransform(NodeTransform nodeTransform) {
        Connective result = Connective.newBuilder()
            .element(ElementUtils.applyNodeTransform(element, nodeTransform))
            .targetVars(defaultTargetVars == null ? null : defaultTargetVars.stream()
                    .map(v -> (Var)nodeTransform.apply(v))
                    .toList())
            .connectVars(connectVars.stream() // Should never be null (I think)
                    .map(v -> (Var)nodeTransform.apply(v))
                    .toList())
            .build();
        return result;
    }

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
     * Returns whether this connective is empty.
     *
     * @return True if empty, false otherwise
     */
    public boolean isEmpty() {
        return element instanceof ElementGroup g
            ? g.isEmpty() && connectVars != null && connectVars.isEmpty() && defaultTargetVars != null && defaultTargetVars.isEmpty()
            : false;
    }

    /**
     * Returns visible vars + variables mentioned in the pattern.
     *
     * @return The set of mentioned variables
     */
    public Set<Var> getMentionedVars() {
        Set<Var> result = new LinkedHashSet<>(visibleVars);
        PatternVars.vars(result, element);
        return result;
    }

    /**
     * Creates a new ConnectiveBuilder.
     *
     * @return A new ConnectiveBuilder instance
     */
    public static ConnectiveBuilder<?> newBuilder() {
        return new ConnectiveBuilder<>();
    }

    /**
     * Creates a new Connective from a path.
     *
     * @param path The path to create the connective from
     * @return A new Connective instance
     */
    public static Connective of(Path path) {
        return Connective.newBuilder().step(path).build();
    }

    /**
     * Creates an empty Connective.
     *
     * @return An empty Connective instance
     */
    public static Connective empty() {
        return Connective.newBuilder()
                .connectVarNames()
                .targetVarNames()
                .element(new ElementGroup())
                .build();
    }

    /**
     * Creates a new Connective from source and target variables and an element.
     *
     * @param sourceVars The source variables
     * @param targetVars The target variables
     * @param elt The graph pattern element
     * @return A new Connective instance
     */
    public static Connective of(List<Var> sourceVars, List<Var> targetVars, Element elt) {
        return Connective.newBuilder()
            .connectVars(sourceVars)
            .targetVars(targetVars)
            .element(elt)
            .build();
    }
}
