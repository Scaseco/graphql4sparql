package org.aksw.graphql4sparql.engine.acc.state.api.builder;

/**
 * Builder interface for aggregators with transition matching.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AggStateBuilderTransition<I, E, K, V>
    extends AggStateBuilder<I, E, K, V>
{
    /**
     * Gets the match state ID.
     *
     * @return The match state ID
     */
    Object getMatchStateId();
}
