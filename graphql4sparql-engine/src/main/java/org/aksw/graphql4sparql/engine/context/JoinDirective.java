package org.aksw.graphql4sparql.engine.context;

import java.util.List;

/**
 * &#064;join(this: ['x', 'y'], parent: ['v', 'w'])
 *
 * @param thisVars Variables in the current scope
 * @param parentVars Variables from the parent scope
 */
public record JoinDirective(List<String> thisVars, List<String> parentVars) {}
