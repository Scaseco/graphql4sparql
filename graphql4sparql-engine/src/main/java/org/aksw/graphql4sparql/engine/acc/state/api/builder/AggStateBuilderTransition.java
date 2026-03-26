package org.aksw.graphql4sparql.engine.acc.state.api.builder;

public interface AggStateBuilderTransition<I, E, K, V>
    extends AggStateBuilder<I, E, K, V>
{
    Object getMatchStateId();
}
