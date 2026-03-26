package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceNode;

/** AggState that does NOT PRODUCE entries. May produce literals, objects or arrays. */
public interface AggStateTypeProduceNode<I, E, K, V>
    extends AggStateGon<I, E, K, V>
{
    @Override
    AccStateTypeProduceNode<I, E, K, V> newAccumulator();
}
