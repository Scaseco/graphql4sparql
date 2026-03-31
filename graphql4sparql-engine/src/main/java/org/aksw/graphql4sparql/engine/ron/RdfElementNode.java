package org.aksw.graphql4sparql.engine.ron;

import org.apache.jena.graph.Node;

public interface RdfElementNode
    extends RdfElement
{
    Node getInternalId();
}
