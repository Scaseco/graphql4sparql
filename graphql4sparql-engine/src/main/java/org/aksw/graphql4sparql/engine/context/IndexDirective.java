package org.aksw.graphql4sparql.engine.context;

import java.util.List;

/**
 * &#064;index(by: varOrVarArray[, expr: exprOverVars])
 * The idea is to allow ordering by a given list of variables, and have a separate expression for the effective output.
 * This may help a DBMS by not having to order by the final expression.
 *
 * @param keyExpr The expression for the key
 * @param indexExprs List of expressions for indexing
 * @param oneIf Expression that determines if only a single result is expected
 */
// Perhaps extend indexVars to exprs
public record IndexDirective(String keyExpr, List<String> indexExprs, String oneIf) { }
