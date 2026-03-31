package org.aksw.graphql4sparql.engine.acc.state.api;

/**
 * Interface for accumulator states that can transition to a sub-state based on a match condition.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AccStateTypeTransition<I, E, K, V>
    extends AccStateGon<I, E, K, V>
{
    /**
     * Gets the match state id.
     *
     * @return The match state id
     */
    Object getMatchStateId();
}
