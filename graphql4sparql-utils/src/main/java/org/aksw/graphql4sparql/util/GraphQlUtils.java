package org.aksw.graphql4sparql.util;

import java.io.PrintStream;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.jena.sparql.expr.NodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.com.google.common.collect.Multimap;
import graphql.com.google.common.collect.Multimaps;
import graphql.language.Argument;
import graphql.language.ArrayValue;
import graphql.language.AstPrinter;
import graphql.language.AstTransformer;
import graphql.language.BooleanValue;
import graphql.language.Directive;
import graphql.language.Directive.Builder;
import graphql.language.DirectivesContainer;
import graphql.language.Document;
import graphql.language.EnumValue;
import graphql.language.Field;
import graphql.language.FieldDefinition;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.Node;
import graphql.language.NodeVisitorStub;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.OperationDefinition;
import graphql.language.OperationDefinition.Operation;
import graphql.language.ScalarValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.language.VariableReference;
import graphql.parser.Parser;
import graphql.parser.ParserEnvironment;
import graphql.parser.ParserOptions;
import graphql.util.TraverserContext;
import graphql.util.TreeTransformerUtil;

/**
 * Utility class for GraphQL operations.
 */
public class GraphQlUtils {

    private GraphQlUtils() {}

    /**
     * Replaces illegal characters in a name with underscores.
     * @param name The name to sanitize
     * @return The sanitized name
     */
    public static String safeName(String name) {
        return replaceIllegalChars(name, "_");
    }

    /**
     * Replaces illegal characters in a name with the given replacement.
     * @param name The name to sanitize
     * @param replacement The replacement string
     * @return The sanitized name
     */
    public static String replaceIllegalChars(String name, String replacement) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (isValidNameChar(c, i)) {
                sb.append(c);
            } else {
                sb.append(replacement);
            }
        }
        String result = sb.toString();
        return result;
    }

    /** Whether the char is valid at the given index.
     * @param c The character to check
     * @param index The index of the character
     * @return true if valid, false otherwise
     */
    public static boolean isValidNameChar(char c, int index) {
        boolean result = index == 0
            ? c == '_' || isAsciiLetter(c)
            : c == '_' || isAsciiLetterOrDigit(c);
        return result;
    }

    /**
     * Checks if a character is an ASCII letter.
     * @param c The character to check
     * @return true if the character is an ASCII letter, false otherwise
     */
    public static boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Checks if a character is an ASCII letter or digit.
     * @param c The character to check
     * @return true if the character is an ASCII letter or digit, false otherwise
     */
    public static boolean isAsciiLetterOrDigit(char c) {
        return isAsciiLetter(c) || Character.isDigit(c);
    }

    private static final Logger logger = LoggerFactory.getLogger(GraphQlUtils.class);

    /**
     * Creates a copy of a node with the given list of directives.
     * Returns the new node.
     *
     * replaceDirectives(node, context, (n, newDirectives) -> n.transform(builder -> builder.directives(newDirectives)), remainingDirectives)
     * @param <T> The node type
     * @param node The node to transform
     * @param context The traverser context
     * @param transform The transformation function
     * @param remainingDirectives The remaining directives to apply
     * @return The transformed node
     */
    public static <T extends Node<T>> T replaceDirectivesOld(T node, TraverserContext<Node> context,
            BiFunction<T, List<Directive>, T> transform, LinkedList<Directive> remainingDirectives) {
        List<Directive> finalRemainingDirectives = remainingDirectives;
        T replacementNode = transform.apply(node, finalRemainingDirectives);
        TreeTransformerUtil.changeNode(context, replacementNode);
        return replacementNode;
    }

    /**
     * Replaces directives on a node using a factory-based transformation.
     * @param <T> The node type
     * @param node The node to transform
     * @param context The traverser context
     * @param transform The transformation factory function
     * @param remainingDirectives The remaining directives to apply
     * @return The transformed node
     */
    public static <T extends Node<T>> T replaceDirectives(T node, TraverserContext<Node> context,
            Function<T, Function<List<Directive>, T>> transform, List<Directive> remainingDirectives) {
        List<Directive> finalRemainingDirectives = remainingDirectives;
        Function<List<Directive>, T> factory = transform.apply(node);
        // T replacementNode = transform.apply(node, finalRemainingDirectives);
        T replacementNode = factory.apply(finalRemainingDirectives);
        TreeTransformerUtil.changeNode(context, replacementNode);
        return replacementNode;
    }

    /**
     * Factory method for setting directives on a field.
     * @param node The field node
     * @return A function that takes a list of directives and returns a transformed field
     */
    public static Function<List<Directive>, Field> directivesSetterField(Field node) {
        return newDirectives -> node.transform(builder -> builder.directives(newDirectives));
    }

    /**
     * Factory method for setting directives on a field definition.
     * @param node The field definition node
     * @return A function that takes a list of directives and returns a transformed field definition
     */
    public static Function<List<Directive>, FieldDefinition> directivesSetterFieldDefinition(FieldDefinition node) {
        return newDirectives -> node.transform(builder -> builder.directives(newDirectives));
    }

    /** FIXME Update to the graphqls pec*/
//    public static boolean isValidCharForFieldName(int ch) {
//        return VarUtils.isValidFirstCharForVarName(ch);
//    }
//
//    /** Replaces any invalid char with '_' and returns null on empty string */
//    public static String safeFieldName(String name) {
//        return VarUtils.safeIdentifier(name, '_', GraphQlUtils::isValidCharForFieldName);
//    }

    /** Check if the document has a query directive with the given name.
     * @param document The GraphQL document
     * @param directiveName The directive name to search for
     * @return true if the document has the directive, false otherwise
     */
    public static boolean hasQueryDirective(Document document, String directiveName) {
        List<Directive> matches = getQueryDirectives(document, directiveName);
        boolean result = !matches.isEmpty();
        return result;
    }

    /**
     * Gets all query directives with the given name from a document.
     * @param document The GraphQL document
     * @param directiveName The directive name
     * @return The list of directives
     */
    public static List<Directive> getQueryDirectives(Document document, String directiveName) {
        List<Directive> result;
        if (document != null) {
            // Find an OperationDefinition of type Query with the given directive name present
            result = document.getDefinitionsOfType(OperationDefinition.class).stream()
                .filter(od -> Operation.QUERY.equals(od.getOperation()))
                .flatMap(od -> od.getDirectives(directiveName).stream())
                .collect(Collectors.toList());
        } else {
            result = List.of();
        }

        return result;
    }

    /**
     * Traverses a node path and returns the node at that location.
     * @param node The root node
     * @param path The path segments to traverse
     * @return The node at the path location, or empty if not found
     */
    public static Optional<Node<?>> tryGetNode(Node<?> node, String ... path) {
        Node<?> result = node;
        for (String segment : path) {
            if (result == null) {
                break;
            }
            result = result.getNamedChildren().getChildOrNull(segment);
        }
        return Optional.ofNullable(result);
    }

    /**
     * Gets a numeric value from a node at the given path.
     * @param node The root node
     * @param path The path to the node
     * @return The number value, or null if not found
     */
   /**
     * Gets a numeric value from a node at the given path.
     * @param node The root node
     * @param path The path to the node
     * @return The number value, or null if not found
     */
    public static Number getNumber(Node<?> node, String ... path) {
        return tryGetNode(node, path).map(GraphQlUtils::toNumber).orElse(null);
    }

    /**
     * Gets a long value from a node at the given path.
     * @param node The root node
     * @param path The path to the node
     * @return The long value, or null if not found
     */
    public static Long getLong(Node<?> node, String ... path) {
        Number number = getNumber(node, path);
        return number == null ? null : number.longValue();
    }

    /**
     * Gets a string value from a node at the given path.
     * @param node The root node
     * @param path The path to the node
     * @return The string value, or null if not found
     */
    public static String getString(Node<?> node, String ... path) {
        return tryGetNode(node, path)
                .map(GraphQlUtils::toNodeValue)
                .map(NodeValue::asNode)
                .map(org.apache.jena.graph.Node::getLiteralLexicalForm)
                .orElse(null);
    }

    /**
     * Converts a GraphQL node to a Jena NodeValue. (The latter also has a nicer API).
     * @param node The GraphQL node
     * @return The NodeValue representation, or null if the node type is not supported
     */
    public static NodeValue toNodeValue(Node<?> node) {
        NodeValue result = null;
        if (node instanceof IntValue) {
            result = NodeValue.makeInteger(((IntValue)node).getValue());
        } else if (node instanceof FloatValue) {
            result = NodeValue.makeDecimal(((FloatValue)node).getValue());
        } else if (node instanceof BooleanValue) {
            result = NodeValue.makeBoolean(((BooleanValue)node).isValue());
        } else if (node instanceof StringValue) {
            result = NodeValue.makeString(((StringValue)node).getValue());
        } else if (node instanceof EnumValue) {
            result = NodeValue.makeString(((EnumValue)node).getName());
        }
        return result;
    }

    /**
     * Converts a Jena NodeValue to a GraphQL ScalarValue.
     * @param nv The NodeValue
     * @return The ScalarValue representation
     * @throws UnsupportedOperationException if the NodeValue type is not supported
     */
    public static ScalarValue<?> toScalarValue(NodeValue nv) {
        ScalarValue<?> result;
        if (nv.isString()) {
            result = StringValue.newStringValue(nv.getString()).build();
        } else  if (nv.isInteger()) {
            result = IntValue.newIntValue(nv.getInteger()).build();
        } else if (nv.isBoolean()) {
            result = BooleanValue.newBooleanValue(nv.getBoolean()).build();
        } else if (nv.isDecimal()) {
            result = FloatValue.newFloatValue(nv.getDecimal()).build();
        } else {
            throw new UnsupportedOperationException("Cannot convert: " + nv);
        }
        return result;
    }

    /**
     * Gets a boolean argument value from a directive.
     * @param directive The directive
     * @param argName The argument name
     * @return The boolean value, or null if not found
     */
    public static Boolean getArgAsBoolean(Directive directive, String argName) {
        Boolean result = getArgAsBoolean(directive, argName, null);
        return result;
    }

    /**
     * Gets a string argument value from a directive.
     * @param directive The directive
     * @param argName The argument name
     * @return The string value, or null if not found
     */
    public static String getArgAsString(Directive directive, String argName) {
        String result = GraphQlUtils.toString(GraphQlUtils.getValue(directive.getArgument(argName)));
        return result;
    }

   /** Expand strings to lists.
     * @param directive The directive
     * @param argName The argument name
     * @return The list of strings or null
     */
    public static List<String> getArgAsStrings(Directive directive, String argName) {
        Value<?> raw = GraphQlUtils.getValue(directive.getArgument(argName));
        List<String> result = raw == null
            ? null
            : raw instanceof ArrayValue arr
                ? arr.getValues().stream().map(v -> toString(v)).toList()
                : List.of(toString(raw));
        return result;
    }

    /**
     * Converts a GraphQL node to a string representation.
     * @param node The GraphQL node
     * @return The string value, or null if not found
     */
    public static String toString(Node<?> node) {
        NodeValue nv = toNodeValue(node);
        String result = nv == null ? null : nv.getString();
        return result;
    }

    /**
     * Converts a GraphQL node to a boolean value.
     * @param node The GraphQL node
     * @return The boolean value, or null if not found
     */
    public static Boolean toBoolean(Node<?> node) {
        NodeValue nv = toNodeValue(node);
        Boolean result = nv == null ? null : nv.getBoolean();
        return result;
    }

    /**
     * Converts a GraphQL node to a number value.
     * @param node The GraphQL node
     * @return The number value, or null if not found
     */
    public static Number toNumber(Node<?> node) {
        NodeValue nv = toNodeValue(node);
        Number result = nv == null ? null : NodeValueUtils.getNumber(nv);
        return result;
    }

    /**
     * Converts a GraphQL node to a long value.
     * @param node The GraphQL node
     * @return The long value, or null if not found
     */
    public static Long toLong(Node<?> node) {
        Number number = toNumber(node);
        Long result = number == null ? null : number.longValue();
        return result;
    }

    /**
     * Indexes the arguments of a field by their names.
     * @param field The field
     * @return A multimap of argument names to values
     */
    public static Multimap<String, Value<?>> indexArguments(Field field) {
        Multimap<String, Value<?>> result = Multimaps.transformValues(
                Multimaps.index(field.getArguments(), Argument::getName), Argument::getValue);
        return result;
        // field.getArguments().stream().collect(null)
    }

    /**
     * Indexes the values of an object value by their field names.
     * @param field The object value
     * @return A multimap of field names to values
     */
    public static Multimap<String, Value<?>> indexValues(ObjectValue field) {
        Multimap<String, Value<?>> result = Multimaps.transformValues(
                Multimaps.index(field.getObjectFields(), ObjectField::getName), ObjectField::getValue);
        return result;
    }

//    public static Value<?> getArgumentValue(Multimap<String, Value<?>> args, String argName) {
//        Collection<Value<?>> a = args.get(argName);
//        Value<?> result = Iterables.getOnlyElement(a, null);
//        //Value<?> result = arg == null ? null : arg.getValue();
//        return result;
//    }

//    public static Value<?> getArgumentValue(Multimap<String, Value<?>> args, String argName, Map<String, Value<?>> assignments) {
//        return resolveValue(getArgumentValue(args, argName), assignments);
//    }
//
//    public static Optional<Value<?>> tryGetArgumentValue(Multimap<String, Value<?>> args, String argName) {
//        Value<?> value = getArgumentValue(args, argName);
//        return Optional.ofNullable(value);
//    }

    /**
     * Gets the value from an argument.
     * @param arg The argument
     * @return The value, or null if the argument is null
     */
    public static Value<?> getValue(Argument arg) {
        return arg == null ? null : arg.getValue();
    }

//    public static TreeDataMap<Path<String>, Field> indexFields(SelectionSet selectionSet) {
//        TreeDataMap<Path<String>, Field> result = new TreeDataMap<>();
//        Path<String> path = PathStr.newAbsolutePath();
//        indexFields(result, path, selectionSet);
//        return result;
//    }
//
//    public static void indexFields(TreeDataMap<Path<String>, Field> tree, Path<String> path, SelectionSet selection) {
//        if (selection != null) {
//            List<Field> list = selection.getSelectionsOfType(Field.class);
//            if (list != null) {
//                for (Field childField : list) {
//                    indexField(tree, path, childField);
//                }
//            }
//        }
//    }

    /**
     * Gets a long argument value from a field with variable resolution.
     * @param field The field
     * @param name The argument name
     * @param assignments The variable assignments map
     * @return The long value, or null if not found
     */
    public static Long getArgAsLong(Field field, String name, Map<String, Value<?>> assignments) {
        return getArgAsLong(field.getArguments(),  name, assignments);
    }

    /**
     * Gets a long argument value from a list of arguments with variable resolution.
     * @param arguments The list of arguments
     * @param name The argument name
     * @param assignments The variable assignments map
     * @return The long value, or null if not found
     */
    public static Long getArgAsLong(List<Argument> arguments, String name, Map<String, Value<?>> assignments) {
        return toLong(resolveValue(getValue(findArgument(arguments, name)), assignments));
    }

    /**
     * Finds an argument by name in a list of arguments.
     * @param arguments The list of arguments
     * @param name The argument name to find
     * @return The argument, or null if not found
     */
    public static Argument findArgument(List<Argument> arguments, String name) {
        return arguments.stream()
                .filter(arg -> name.equals(arg.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the value of an argument from a directive.
     * @param directive The directive
     * @param argName The argument name
     * @return The value, or null if the argument is not found
     */
    public static Value<?> getArgValue(Directive directive, String argName) {
        Argument arg = directive.getArgument(argName);
        Value<?> result = arg == null ? null : arg.getValue();
        return result;
    }

    /**
     * Gets the string value of an argument from a directive with variable resolution.
     * @param directive The directive
     * @param argName The argument name
     * @param assignments The variable assignments map
     * @return The string value, or null if not found
     */
    public static String getArgValueAsString(Directive directive, String argName, Map<String, Value<?>> assignments) {
        String result = toString(resolveValue(getValue(directive.getArgument(argName)), assignments));
        return result;
    }

    /**
     * Gets a boolean argument value from a directive with variable resolution.
     * @param directive The directive
     * @param argName The argument name
     * @param assignments The variable assignments map
     * @return The boolean value, or null if not found
     */
    public static Boolean getArgAsBoolean(Directive directive, String argName, Map<String, Value<?>> assignments) {
        Boolean result = toBoolean(resolveValue(getValue(directive.getArgument(argName)), assignments));
        return result;
    }

//    public static void indexField(TreeDataMap<Path<String>, Field> tree, Path<String> path, Field field) {
//        String fieldName = field.getName();
//        Path<String> fieldPath = path.resolve(fieldName);
//        tree.putItem(fieldPath, Path::getParent);
//        tree.put(fieldPath, field);
//        SelectionSet selectionSet = field.getSelectionSet();
//        indexFields(tree, fieldPath, selectionSet);
//    }

    /**
     * Resolves variable references once against the given map of assignments. Null if there is no assignment.
     * @param value The value
     * @param assignments The assignments map
     * @return The resolved value or null
     */
    /**
     * Resolves a value by replacing variable references with their assigned values.
     * @param value The value to resolve
     * @param assignments The variable assignments map
     * @return The resolved value, or null if a variable has no assignment
     */
    public static Value<?> resolveValue(Value<?> value, Map<String, Value<?>> assignments) {
        Value<?> result;
        if (value instanceof VariableReference ref) {
            String varName = ref.getName();
            result = assignments.get(varName);
        } else {
            result = value;
        }
        return result;
    }

    /**
     * Converts a map of Jena Nodes to a map of GraphQL values.
     * @param assignments The map of Jena Nodes
     * @return The map of GraphQL values, or null if the input is null
     */
    public static Map<String, Value<?>> mapToGraphQl(Map<String, org.apache.jena.graph.Node> assignments) {
        Map<String, Value<?>> result = assignments == null
                ? null
                : assignments.entrySet().stream().collect(Collectors.toMap(
                        Entry::getKey, v -> (Value<?>)GraphQlUtils.toScalarValue(NodeValue.makeNode(v.getValue()))));
        return result;
    }

    /**
     * Converts a map of GraphQL values to a map of Jena Nodes.
     * @param assignments The map of GraphQL values
     * @return The map of Jena Nodes, or null if the input is null
     */
    public static Map<String, org.apache.jena.graph.Node> mapToJena(Map<String, Value<?>> assignments) {
        Map<String, org.apache.jena.graph.Node> result = assignments == null
                ? null
                : assignments.entrySet().stream().collect(Collectors.toMap(
                        Entry::getKey, v -> GraphQlUtils.toNodeValue(v.getValue()).asNode()));
        return result;
    }


    /**
     * Applies a transformation to a GraphQL document.
     * @param doc The document to transform
     * @param visitor The visitor to apply
     * @return The transformed document
     */
    public static Document applyTransform(Document doc, NodeVisitorStub visitor) {
        AstTransformer transformer = new AstTransformer();
        Node<?> node = transformer.transform(doc, visitor);
        Document result = (Document)node;
        return result;
    }

    /**
     * Prints a GraphQL document to the given print stream.
     * @param printStream The print stream
     * @param doc The document to print
     */
    public static void println(PrintStream printStream, Document doc) {
        String str = AstPrinter.printAst(doc);
        printStream.println(str);
    }

    /**
     * Expects at most one directive with the given name on a container.
     * Logs a warning if multiple directives are found and returns the last one.
     * @param container The directives container
     * @param name The directive name
     * @return The directive, or null if not found
     */
    public static Directive expectAtMostOneDirective(DirectivesContainer<?> container, String name) {
        List<Directive> directives = container.getDirectives(name);
        if (directives.size() > 1) {
            // TODO log error to graphql processor and return last
            logger.warn("Only one directive expected: " + name);
        }
        return directives.isEmpty() ? null : directives.get(directives.size() - 1);
    }

//
//    public static Object transform(Node root, NodeVisitor enterVisitor, NodeVisitor leaveVisitor) {
//        assertNotNull(root);
//        assertNotNull(enterVisitor);
//        assertNotNull(leaveVisitor);
//
//        TraverserVisitor<Node> traverserVisitor = new TraverserVisitor<Node>() {
//            @Override
//            public TraversalControl enter(TraverserContext<Node> context) {
//                return context.thisNode().accept(context, enterVisitor);
//            }
//
//            @Override
//            public TraversalControl leave(TraverserContext<Node> context) {
//                return context.thisNode().accept(context, leaveVisitor);
//            }
//        };
//
//
//        // TraverserVisitor<Node> traverserVisitor = AstTransformer.getNodeTraverserVisitor(nodeVisitor);
//        TreeTransformer<Node> treeTransformer = new TreeTransformer<>(AST_NODE_ADAPTER);
//        return treeTransformer.transform(root, traverserVisitor);
//    }


//    public static Directive expectOneDirective(DirectivesContainer<?> container, String name) {
//    	Directive result = expectAtMostOneDirective(container, name);
//    	if (result == null) {
//    		thro
//    	}
//    }

    /**
     * Converts a list of strings to an array value.
     * @param strs The list of strings
     * @return The array value
     */
    public static ArrayValue toArrayValue(List<String> strs) {
        return ArrayValue.newArrayValue().values(strs.stream().map(x -> (Value)StringValue.of(x)).toList()).build();
    }

    /**
     * Creates a new string argument.
     * @param name The argument name
     * @param value The string value
     * @return The argument, or null if value is null
     */
    public static Argument newArgString(String name, String value) {
        return value == null ? null : Argument.newArgument()
                .name(name)
                .value(StringValue.of(value))
                .build();
    }

    /**
     * Creates a new boolean argument.
     * @param name The argument name
     * @param value The boolean value
     * @return The argument, or null if value is null
     */
    public static Argument newArgBoolean(String name, Boolean value) {
        return value == null ? null : Argument.newArgument()
                .name(name)
                .value(BooleanValue.of(value))
                .build();
    }

    /**
     * Creates a new string argument from a list of values.
     * @param name The argument name
     * @param values The list of string values
     * @return The argument, or null if values is null
     */
    public static Argument newArgString(String name, List<String> values) {
        return values == null ? null : Argument.newArgument()
                .name(name)
                .value(toArrayValue(values))
                .build();
    }

    /**
     * Creates a new directive with the given name and arguments.
     * @param name The directive name
     * @param arguments The directive arguments
     * @return The directive
     */
    public static Directive newDirective(String name, Argument... arguments) {
        Builder builder = Directive.newDirective()
                .name(name);
        for (Argument arg : arguments) {
            if (arg != null) {
                builder = builder.argument(arg);
            }
        }
        return builder.build();
    }

    /**
     * Removes all directives with the given name from a collection.
     * @param directives The directives collection
     * @param name The directive name to remove
     */
    public static void removeDirectivesByName(Collection<Directive> directives, String name) {
        directives.removeIf(d -> d.getName().equals(name));
    }


    /**
     * Parses a GraphQL document from a reader without restrictions.
     * @param reader The reader
     * @return The parsed document
     */
    public static Document parseUnrestricted(Reader reader) {
        return parseUnrestricted(builder -> builder.document(reader));
    }

    /**
     * Parses a GraphQL document from a string without restrictions.
     * @param documentStr The document string
     * @return The parsed document
     */
    public static Document parseUnrestricted(String documentStr) {
        return parseUnrestricted(builder -> builder.document(documentStr));
    }

    /**
     * Parses a GraphQL document without restrictions using a builder callback.
     * @param builderCallback The parser environment builder callback
     * @return The parsed document
     */
    public static Document parseUnrestricted(Function<ParserEnvironment.Builder, ? extends ParserEnvironment.Builder> builderCallback) {
        ParserOptions parserOptions = ParserOptions.newParserOptions()
            .maxTokens(Integer.MAX_VALUE)
            .maxRuleDepth(Integer.MAX_VALUE)
            .maxWhitespaceTokens(Integer.MAX_VALUE)
            .maxCharacters(Integer.MAX_VALUE)
            .build();

        ParserEnvironment.Builder parserEnvBuilder = ParserEnvironment.newParserEnvironment()
            .parserOptions(parserOptions);

        parserEnvBuilder = builderCallback.apply(parserEnvBuilder);

        ParserEnvironment parserEnv = parserEnvBuilder.build();
        Document result = Parser.parse(parserEnv);
        return result;
    }
}
