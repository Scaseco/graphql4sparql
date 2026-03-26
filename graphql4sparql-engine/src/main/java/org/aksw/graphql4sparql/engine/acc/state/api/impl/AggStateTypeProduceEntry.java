package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceEntry;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

public interface AggStateTypeProduceEntry<I, E, K, V>
    extends AggStateTransition<I, E, K, V>
{
    @Override
    default GonType getGonType() {
        return GonType.ENTRY;
    }

    /** An edge-based aggregator must declare which state id it matches */

    // AggStateTypeProduceEntry<I, E, K, V> setTargetAgg(AggStateTypeProduceNode<I, E, K, V> targetAgg);
    // AggJsonEdge setSingle(boolean value);

    @Override
    AccStateTypeProduceEntry<I, E, K, V> newAccumulator();
}
