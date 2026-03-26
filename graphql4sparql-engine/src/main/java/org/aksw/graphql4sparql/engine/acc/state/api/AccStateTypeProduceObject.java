package org.aksw.graphql4sparql.engine.acc.state.api;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/** Accumulator that produces objects. */
public interface AccStateTypeProduceObject<I, E, K, V>
    extends AccStateTypeProduceNode<I, E, K, V>
{
    @Override
    default GonType getGonType() {
        return GonType.OBJECT;
    }
    // Object getValue();
}
