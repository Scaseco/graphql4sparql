package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Base class for aggregator states that transition on a state ID.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStateTransitionBase<I, E, K, V>
    implements AggStateTransition<I, E, K, V>
{
    /** The match state ID */
    protected Object matchStateId;

    /**
     * Creates a new AggStateTransitionBase.
     *
     * @param matchStateId The match state ID
     */
    public AggStateTransitionBase(Object matchStateId) {
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
}
