package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Aggregator state for GON output.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AggStateGon<I, E, K, V>
    extends AggState<I, E>
{
    /**
     * Returns the GON type.
     *
     * @return The GON type
     */
    GonType getGonType();

    @Override
    AccStateGon<I, E, K, V> newAccumulator();
}
