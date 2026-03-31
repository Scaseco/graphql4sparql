package org.aksw.graphql4sparql.engine.ron;

import org.apache.jena.graph.Node;

public interface RdfElementResource
    extends RdfElementNode
{
    Node getExternalId();
}
