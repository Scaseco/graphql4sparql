package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

public interface AggStateGon<I, E, K, V>
    extends AggState<I, E>
{
    GonType getGonType();

    @Override
    AccStateGon<I, E, K, V> newAccumulator();
}
