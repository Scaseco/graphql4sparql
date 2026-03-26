package org.aksw.graphql4sparql.engine.acc.state.api.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccState;

public interface AggState<I, E> {
    AccState<I, E> newAccumulator();
}
