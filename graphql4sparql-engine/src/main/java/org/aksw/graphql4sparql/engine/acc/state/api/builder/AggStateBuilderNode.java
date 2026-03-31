package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTypeProduceNode;

/**
 * Builder interface for node aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
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
     * @return The new aggregator
     * @throws UnsupportedOperationException if the conversion is unsupported
     */
    @Override
    AggStateTypeProduceNode<I, E, K, V> newAggregator();
}
