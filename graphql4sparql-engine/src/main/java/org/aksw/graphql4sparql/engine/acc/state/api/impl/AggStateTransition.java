package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeTransition;

/**
 * Base interface for aggregator states that transition on a state id.
 * Edge and fragment (condition) implementations.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AggStateTransition<I, E, K, V>
    extends AggStateGon<I, E, K, V>
{
    /**
     * Returns the match state ID.
     *
     * @return The match state ID
     */
    Object getMatchStateId();

    // AggStateTypeProduceEntry<I, E, K, V> setTargetAgg(AggStateTypeProduceNode<I, E, K, V> targetAgg);
    // AggJsonEdge setSingle(boolean value);
    @Override
    AccStateTypeTransition<I, E, K, V> newAccumulator();
}
