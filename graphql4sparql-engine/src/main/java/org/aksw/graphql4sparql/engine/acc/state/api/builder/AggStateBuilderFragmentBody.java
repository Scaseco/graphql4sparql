package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;
import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateFragmentBody;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;


/**
 * If the state matches, then a set of fields may be emitted.
 * Does not emit an enclosing object.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderFragmentBody<I, E, K, V>
    extends AggStateBuilderObjectLikeBase<I, E, K, V>
{
    @Override
    public GonType getGonType() {
        return GonType.ENTRY;
    }

    /**
     * Creates a new AggStateBuilderFragmentBody.
     */
    public AggStateBuilderFragmentBody() {
        super();
    }

    @Override
    public AggStateFragmentBody<I, E, K, V> newAggregator() {
        AggStateTransition<I, E, K, V>[] subAggs = buildSubAggs();
        AggStateFragmentBody<I, E, K, V> result = AggStateFragmentBody.of(subAggs);
        return result;
    }
}
