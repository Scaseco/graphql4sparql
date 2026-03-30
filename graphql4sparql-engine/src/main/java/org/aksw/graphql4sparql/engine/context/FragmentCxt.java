package org.aksw.graphql4sparql.engine.context;

import java.util.List;

import org.apache.jena.sparql.core.Var;

import graphql.language.Node;

/**
 * A fragment context.
 *
 * @param startNode The start node
 * @param connectVars The connection variables
 */
public record FragmentCxt(Node startNode,  List<Var> connectVars) {
}
