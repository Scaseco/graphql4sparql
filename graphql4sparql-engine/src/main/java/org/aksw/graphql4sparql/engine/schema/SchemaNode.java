package org.aksw.graphql4sparql.engine.schema;

import java.util.Collection;
import java.util.Optional;

public interface SchemaNode {
    Optional<SchemaEdge> getEdge(String name);
    Collection<SchemaEdge> listEdges();
    Fragment getFragment();
}
