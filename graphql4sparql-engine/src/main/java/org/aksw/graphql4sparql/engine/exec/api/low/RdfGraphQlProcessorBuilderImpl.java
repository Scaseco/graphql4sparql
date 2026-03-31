package org.aksw.graphql4sparql.engine.exec.api.low;

import org.aksw.graphql4sparql.engine.rewrite.GraphQlToSparqlConverterBase;

public class RdfGraphQlProcessorBuilderImpl<K>
    extends RdfGraphQlProcessorBuilderBase<K>
{
    public RdfGraphQlProcessorBuilderImpl(GraphQlToSparqlConverterBuilder<K> converterBuilder) {
        super(converterBuilder);
    }

    // protected Creator<? extends GraphQlToSparqlConverterBuilder<K>> converterBuilder;

//    public RdfGraphQlProcessorBuilderImpl(Creator<? extends GraphQlToSparqlConverterBuilder<K>> converterBuilder) {
//        super();
//        this.converterBuilder = Objects.requireNonNull(converterBuilder);
//    }

    @Override
    public GraphQlProcessor<K> build() {
        GraphQlToSparqlConverterBase<K> converter = converterBuilder.build();
        return GraphQlProcessor.of(document, assignments, converter);
    }
}
