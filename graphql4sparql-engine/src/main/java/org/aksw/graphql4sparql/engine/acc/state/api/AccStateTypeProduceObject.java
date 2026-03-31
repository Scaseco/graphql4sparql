package org.aksw.graphql4sparql.engine.acc.state.api;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Accumulator that produces objects.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AccStateTypeProduceObject<I, E, K, V>
    extends AccStateTypeProduceNode<I, E, K, V>
{
    @Override
    default GonType getGonType() {
        return GonType.OBJECT;
    }
    // Object getValue();
}
