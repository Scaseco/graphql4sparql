package org.aksw.graphql4sparql.engine.context;

import java.util.List;

/** Expression by which to filter the parent resources. */
public record ConditionDirective(String whenExprStr, String byExprStr, List<String> thisVars, List<String> parentVars) {}
