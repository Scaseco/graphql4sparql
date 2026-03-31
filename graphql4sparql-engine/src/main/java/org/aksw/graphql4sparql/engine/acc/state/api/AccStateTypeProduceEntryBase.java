package org.aksw.graphql4sparql.engine.acc.state.api;

/**
 * Base class for produce entry accumulator states.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AccStateTypeProduceEntryBase<I, E, K, V>
    extends AccStateBase<I, E, K, V>
    implements AccStateTypeProduceEntry<I, E, K, V>
{
    /** The ancestor state for backtracking. */
    protected AccStateGon<I, E, K, V> ancestorState;

    /**
     * Creates a new AccStateTypeProduceEntryBase.
     */
    public AccStateTypeProduceEntryBase() {
        super();
    }

    /**
     * Sets the ancestor state for backtracking.
     *
     * @param ancestorState The ancestor state to set
     */
    public void setAncestorState(AccStateGon<I, E, K, V> ancestorState) {
        this.ancestorState = ancestorState;
    }
}
