package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateBase;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceEntry;

/**
  * Base class for transition-based accumulator states.
  *
  * @param <I> The input type
  * @param <E> The environment type
  * @param <K> The key type
  * @param <V> The value type
  */
public abstract class AccStateTransitionBase<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeProduceEntry<I, E, K, V>
{
    /** The match state id. */
    protected Object matchStateId;

    /**
     * Creates a new AccStateTransitionBase.
     *
     * @param matchStateId The match state id
     */
    public AccStateTransitionBase(Object matchStateId) {
        super();
        this.matchStateId = matchStateId;
    }

    @Override
    public Object getMatchStateId() {
        return matchStateId;
    }
}
