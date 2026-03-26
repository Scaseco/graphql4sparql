package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTypeProduceNode;

public interface AggStateBuilderNode<I, E, K, V>
    extends AggStateBuilder<I, E, K, V>
{
//    default GraphToJsonNodeMapperObject asObject() {
//        return (GraphToJsonNodeMapperObject)this;
//    }
//
//    default GraphToJsonNodeMapperLiteral asLiteral() {
//        return (GraphToJsonNodeMapperLiteral)this;
//    }

    /**
     * Attempt to convert the mapper into an aggregator that can assemble
     * json from an ordered stream of triples.
     *
     * @throws UnsupportedOperationException if the conversion is unsupported
     */
    @Override
    AggStateTypeProduceNode<I, E, K, V> newAggregator();
}
