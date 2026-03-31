package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccState;

/**
 * Aggregator state interface.
 *
 * @param <I> The input type
 * @param <E> The environment type
 */
public interface AggState<I, E> {
    /**
     * Creates a new accumulator.
     *
     * @return The new accumulator
     */
    AccState<I, E> newAccumulator();
}
