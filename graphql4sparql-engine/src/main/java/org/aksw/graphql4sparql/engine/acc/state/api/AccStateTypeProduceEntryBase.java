package org.aksw.graphql4sparql.engine.acc.state.api;

public abstract class AccStateTypeProduceEntryBase<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeProduceEntry<I, E, K, V>
{
    protected AccStateGon<I, E, K, V> ancestorState;

    public AccStateTypeProduceEntryBase() {
        super();
    }

    public void setAncestorState(AccStateGon<I, E, K, V> ancestorState) {
        this.ancestorState = ancestorState;
    }
}
