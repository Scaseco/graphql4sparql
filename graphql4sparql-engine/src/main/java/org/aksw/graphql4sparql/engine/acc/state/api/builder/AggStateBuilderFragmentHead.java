package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateFragmentHead;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Builder for fragment head aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderFragmentHead<I, E, K, V>
    extends AggStateBuilderTransitionBase<I, E, K, V>
{
    /**
     * Creates a new AggStateBuilderFragmentHead.
     *
     * @param matchStateId The match state ID
     */
    public AggStateBuilderFragmentHead(Object matchStateId) { //, AggStateBuilder<I, E, K, V> subAggBuilder) {
        super(matchStateId);
    }

    @Override
    protected void validateTargetBuilder(AggStateBuilder<I, E, K, V> targetBuilder) {
        GonType targetType = targetBuilder.getGonType();
        if (!GonType.ENTRY.equals(targetType)) {
            throw new RuntimeException("Target builder for fragment head must have gon type ENTRY, got: " + targetType);
        }
    }

    @Override
    public AggStateFragmentHead<I, E, K, V> newAggregator() {
        // AggStateTransition<I, E, K, V>[] subAggs = buildSubAggs();
        AggStateGon<I, E, K, V> subAgg = targetNodeMapper.newAggregator();
        AggStateFragmentHead<I, E, K, V> result = AggStateFragmentHead.of(matchStateId, subAgg);
        return result;
    }
}
