package org.aksw.graphql4sparql.engine.util;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprTransformer;
import org.apache.jena.sparql.graph.NodeTransform;
import org.apache.jena.sparql.graph.NodeTransformExpr;

/**
 * Utility class for working with SPARQL expressions.
 */
public class ExprUtils {
    private ExprUtils() {}

    /**
     * Node transform version that
     * (a) handles blank nodes correctly; in constrast to Expr.applyNodeTransform
     * [disabled (b) treats null mappings as identity mapping]
     *
     * @param expr The expression
     * @param xform The node transform
     * @return The transformed expression
     */
    public static Expr applyNodeTransform(Expr expr, NodeTransform xform) {
        Expr result = ExprTransformer.transform(new NodeTransformExpr(node -> {
            Node r = xform.apply(node);
            //Node r = Optional.ofNullable(xform.apply(node)).orElse(node);
            return r;
        }), expr);
        return result;
    }

}
