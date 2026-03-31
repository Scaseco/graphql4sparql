package org.aksw.graphql4sparql.engine.ron;

import org.apache.jena.graph.Node;

public interface RdfLiteral
    extends RdfElement
{
    Node getInternalId();
}
