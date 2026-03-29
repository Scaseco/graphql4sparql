package org.aksw.graphql4sparql.engine.context;

import java.util.List;

/**
 * Expression by which to filter the parent resources.
 *
 * @param whenExprStr Expression that determines if the filter should be applied
 * @param byExprStr The filter expression
 * @param thisVars Variables in the current scope
 * @param parentVars Variables from the parent scope
 */
public record ConditionDirective(String whenExprStr, String byExprStr, List<String> thisVars, List<String> parentVars) {}
