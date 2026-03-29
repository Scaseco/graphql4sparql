package org.aksw.graphql4sparql.engine.agg.state.impl;

import java.util.Objects;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeTransition;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AccStateCondition;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Aggregator state for conditional transitions.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateCondition<I, E, K, V>
    implements AggStateTransition<I, E, K, V>
{
    /** The match state ID. */
    protected Object matchStateId;
    /** The sub aggregator. */
    protected AggStateGon<I, E, K, V> subAgg;

    /**
     * Creates a new AggStateCondition.
     *
     * @param matchStateId The match state ID
     * @param subAgg The sub aggregator
     */
    protected AggStateCondition(Object matchStateId, AggStateGon<I, E, K, V> subAgg) {
        super();
        this.matchStateId = Objects.requireNonNull(matchStateId);
        this.subAgg = Objects.requireNonNull(subAgg);
    }

    /**
     * Creates a new AggStateCondition.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchStateId The match state ID
     * @param subAgg The sub aggregator
     * @return The new AggStateCondition
     */
    public static <I, E, K, V> AggStateCondition<I, E, K, V> of(Object matchStateId, AggStateGon<I, E, K, V> subAgg) {
        return new AggStateCondition<>(matchStateId, subAgg);
    }

    @Override
    public Object getMatchStateId() {
        return matchStateId;
    }

    @Override
    public GonType getGonType() {
        return subAgg.getGonType();
    }

    @Override
    public AccStateTypeTransition<I, E, K, V> newAccumulator() {
        AccStateGon<I, E, K, V> subAcc = subAgg.newAccumulator();
        return new AccStateCondition<>(matchStateId, subAcc);
    }
}
