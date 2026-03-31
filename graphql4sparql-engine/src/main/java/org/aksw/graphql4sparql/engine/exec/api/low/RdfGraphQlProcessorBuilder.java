package org.aksw.graphql4sparql.engine.exec.api.low;

public interface RdfGraphQlProcessorBuilder<K>
    extends GraphQlProcessorSettings<RdfGraphQlProcessorBuilder<K>>
{
    GraphQlProcessor<K> build();
}
