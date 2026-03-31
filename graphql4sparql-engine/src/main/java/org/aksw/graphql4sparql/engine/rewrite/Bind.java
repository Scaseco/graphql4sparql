package org.aksw.graphql4sparql.engine.rewrite;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.riot.out.NodeFmtLib;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionEnv;

/**
 * Utility class for creating binding mappers that extract or compute values from SPARQL bindings.
 */
public class Bind {
    private Bind() {
    }

    /**
     * Function interface for mapping a Binding and FunctionEnv to a result type.
     * @param <T> The result type
     */
    interface BindingMapper<T>
        extends BiFunction<Binding, FunctionEnv, T> {
    }

    /**
     * Mapper that extracts a variable's value from a binding.
     */
    public static class BindingMapperVar
        implements BindingMapper<Node>
    {
        /**
         * The variable to extract from the binding.
         */
        protected Var var;

        /**
         * Creates a new BindingMapperVar for the given variable.
         *
         * @param var The variable to map
         */
        public BindingMapperVar(Var var) {
            super();
            this.var = var;
        }

        @Override
        public Node apply(Binding t, FunctionEnv u) {
            return t.get(var);
        }

        @Override
        public String toString() {
            return "Binder(" + var + ")";
        }
    }

    /**
     * Mapper that evaluates an expression and returns the result as a node.
     */
    public static class BindingMapperExpr
        implements BindingMapper<Node>
    {
        /**
         * The expression to evaluate.
         */
        protected Expr expr;

        /**
         * Creates a new BindingMapperExpr for the given expression.
         *
         * @param expr The expression to map
         */
        public BindingMapperExpr(Expr expr) {
            super();
            this.expr = expr;
        }

        @Override
        public Node apply(Binding t, FunctionEnv u) {
            NodeValue nv = expr.eval(t, u);
            Node result = nv == null ? null : nv.asNode();
            return result;
        }

        @Override
        public String toString() {
            return "Binder(" + expr + ")";
        }
    }


    /**
     * Creates a mapper for the variable with the given name.
     *
     * @param varName The variable name
     * @return A new BindingMapperVar
     */
    public static BindingMapperVar var(String varName) {
        return var(Var.alloc(varName));
    }

    /**
     * Creates a mapper for the given variable.
     *
     * @param var The variable to map
     * @return A new BindingMapperVar
     */
    public static BindingMapperVar var(Var var) {
        return new BindingMapperVar(var);
    }


    /**
     * Mapper that extracts multiple variables and returns them as a space-separated string.
     */
    public static class BindingMapperVars
        implements BindingMapper<Node>
    {
        /**
         * The list of variables to extract.
         */
        protected List<Var> vars;

        /**
         * Creates a new BindingMapperVars for the given variables.
         *
         * @param vars The list of variables to map
         */
        public BindingMapperVars(List<Var> vars) {
            super();
            this.vars = vars;
        }

        @Override
        public Node apply(Binding t, FunctionEnv u) {
            List<Node> nodes = vars.stream().map(t::get).toList();
            // NodeList nodeList = new NodeListImpl(nodes);
            // HACK We don't have the list datatype here
            String str = nodes.stream().map(n -> n == null ? "UNDEF" : fmtNode(n)).collect(Collectors.joining(" "));
            return NodeFactory.createLiteralString(str);
        }

        @Override
        public String toString() {
            return "Binder(" + vars + ")";
        }

        /**
         * Formats a node as a string using turtle format for numbers and n-triples otherwise.
         *
         * @param node The node to format
         * @return The formatted string representation
         */
        public static String fmtNode(Node node) {
            // Format numbers using turtle - otherwise use n-triples
            return node.isLiteral() && node.getLiteralValue() instanceof Number
                    ? NodeFmtLib.strTTL(node)
                    : NodeFmtLib.strNT(node);
        }
    }


    /**
     * Creates a mapper for the given variable names.
     *
     * @param varNames The variable names to map
     * @return A new BindingMapperVars
     */
    public static BindingMapperVars vars(String ... varNames) {
        List<Var> list = Var.varList(Arrays.asList(varNames));
        return vars(list);
    }

    /**
     * Creates a mapper for the given variables.
     *
     * @param vars The variables to map
     * @return A new BindingMapperVars
     */
    public static BindingMapperVars vars(List<Var> vars) {
        return new BindingMapperVars(vars);
    }

    /**
     * Creates a mapper that evaluates the given expression.
     *
     * @param expr The expression to map
     * @return A new BindingMapperExpr
     */
    public static BindingMapperExpr expr(Expr expr) {
        return new BindingMapperExpr(expr);
    }

    /**
     * Creates a mapper that always returns true.
     *
     * @return A new BindingMapperExpr that returns NodeValue.TRUE
     */
    public static BindingMapperExpr TRUE() {
        return new BindingMapperExpr(NodeValue.TRUE);
    }

    /**
     * Creates a mapper that always returns false.
     *
     * @return A new BindingMapperExpr that returns NodeValue.FALSE
     */
    public static BindingMapperExpr FALSE() {
        return new BindingMapperExpr(NodeValue.FALSE);
    }
}
