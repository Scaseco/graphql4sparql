package org.aksw.graphql4sparql.engine.exec.api.low;

public interface RdfGraphQlProcessorFactory<K> {
    RdfGraphQlProcessorBuilder<K> newBuilder();
}
