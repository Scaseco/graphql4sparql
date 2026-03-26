package org.aksw.graphql4sparql.engine.acc.state.api;

public interface AccStateTypeTransition<I, E, K, V>
    extends AccStateGon<I, E, K, V>
{
    Object getMatchStateId();
}
