package org.aksw.graphql4sparql.engine.exec.api.low;

import org.apache.jena.atlas.lib.Creator;
import org.apache.jena.sparql.exec.QueryExecBuilder;

public interface GraphQlFieldExecBuilder<K> {
    GraphQlFieldExecBuilder<K> service(Creator<QueryExecBuilder> queryExecBuilderCreator);
    GraphQlFieldExec<K> build();
}
