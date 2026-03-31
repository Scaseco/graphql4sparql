package org.aksw.graphql4sparql.engine.context;

import java.util.Optional;

import org.aksw.graphql4sparql.util.GraphQlUtils;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.util.ExprUtils;

import graphql.language.Directive;

/**
 * A directive for binding expressions to variables in SPARQL queries.
 *
 * @param exprStr The expression string to parse and execute
 * @param varName The variable name to bind the result to
 * @param isTarget Whether this binding is a target binding
 */
public record BindDirective(String exprStr, String varName, Boolean isTarget)
    implements PrefixExpandable<BindDirective>
{
    public static final DirectiveParser<BindDirective> PARSER = new DirectiveParserImpl<>(BindDirective.class, "bind", false, BindDirective::parse);

    /**
     * Parses a directive into a BindDirective instance.
     *
     * @param directive The GraphQL directive to parse
     * @return A BindDirective instance
     */
    public static BindDirective parse(Directive directive) {
        String exprStr = GraphQlUtils.getArgAsString(directive, "of");
        String as = GraphQlUtils.getArgAsString(directive, "as");
        boolean isTarget = Optional.ofNullable(GraphQlUtils.getArgAsBoolean(directive, "target")).orElse(false);
        BindDirective result = new BindDirective(exprStr, as, isTarget);
        return result;
    }

    /**
     * Returns the variable for this binding.
     *
     * @return The variable, or null if varName is null
     */
    public Var getVar() {
        return varName == null ? null : Var.alloc(varName);
    }

    /**
     * Parses the expression string into an Expr object.
     *
     * @return The parsed expression
     */
    public Expr parseExpr() {
        Expr result = ExprUtils.parse(exprStr);
        return result;
    }

    @Override
    public BindDirective expand(PrefixMapping pm) {
        Expr expr = ExprUtils.parse(exprStr, pm);
        String newExprStr = ExprUtils.fmtSPARQL(expr);
        return new BindDirective(newExprStr, varName, isTarget);
    }

    /**
     * Converts this BindDirective back to a GraphQL directive.
     *
     * @return The directive representation
     */
    public Directive toDirective() {
        return GraphQlUtils.newDirective("bind",
            GraphQlUtils.newArgString("of", exprStr),
            GraphQlUtils.newArgString("as", varName),
            GraphQlUtils.newArgBoolean("target", isTarget));
    }
}
