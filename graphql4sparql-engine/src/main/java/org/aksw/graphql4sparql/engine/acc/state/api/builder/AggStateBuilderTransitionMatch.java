package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/** Builder base class for aggregators that match on a state id. */
public abstract class AggStateBuilderTransitionMatch<I, E, K, V>
    implements AggStateBuilderTransition<I, E, K, V>
{
    protected Object matchStateId;

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
