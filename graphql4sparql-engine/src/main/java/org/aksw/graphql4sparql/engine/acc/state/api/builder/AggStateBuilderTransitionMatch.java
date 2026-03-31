package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Builder base class for aggregators that match on a state id.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStateBuilderTransitionMatch<I, E, K, V>
    implements AggStateBuilderTransition<I, E, K, V>
{
    /**
     * The match state ID.
     */
    protected Object matchStateId;

    /**
     * Creates a new AggStateBuilderTransitionMatch.
     *
     * @param matchStateId The match state ID
     */
    public AggStateBuilderTransitionMatch(Object matchStateId) {
        super();
        this.matchStateId = matchStateId;
    }

    @Override
    public GonType getGonType() {
        return GonType.ENTRY;
    }

    @Override
    public Object getMatchStateId() {
        return matchStateId;
    }

    @Override
    public abstract AggStateTransition<I, E, K, V> newAggregator();
}
