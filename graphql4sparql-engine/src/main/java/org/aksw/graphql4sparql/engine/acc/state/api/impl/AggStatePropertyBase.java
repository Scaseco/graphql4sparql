package org.aksw.graphql4sparql.engine.acc.state.api.impl;

/**
 * Base class for aggregator states for properties.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStatePropertyBase<I, E, K, V>
    extends AggStateTransitionBase<I, E, K, V>
    implements AggStateTypeProduceEntry<I, E, K, V>
{
    /** The member key being aggregated */
    protected K memberKey;
    /** Whether the property is single-valued */
    protected boolean isSingle = false;
    /** The array mode */
    protected ArrayMode arrayMode;

    /**
     * Creates a new AggStatePropertyBase.
     *
     * @param matchStateId The match state ID
     * @param memberKey The member key
     * @param isSingle Whether the property is single-valued
     * @param arrayMode The array mode
     */
    protected AggStatePropertyBase(Object matchStateId, K memberKey, boolean isSingle, ArrayMode arrayMode) {
        super(matchStateId);
        this.matchStateId = matchStateId;
        this.memberKey = memberKey;
        this.isSingle = isSingle;
        this.arrayMode = arrayMode;
    }

    /**
     * Returns the member key.
     *
     * @return The member key
     */
    public K getMemberKey() {
        return memberKey;
    }

    /**
     * Returns whether the property is single-valued.
     *
     * @return True if single-valued, false otherwise
     */
    public boolean isSingle() {
        return isSingle;
    }

    /**
     * Returns the array mode.
     *
     * @return The array mode
     */
    public ArrayMode getArrayMode() {
        return arrayMode;
    }
}
