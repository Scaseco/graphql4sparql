package org.aksw.graphql4sparql.engine.context;

import java.util.List;

/** &#064;join(this: ['x', 'y'], parent: ['v', 'w']) */
public record JoinDirective(List<String> thisVars, List<String> parentVars) {}
