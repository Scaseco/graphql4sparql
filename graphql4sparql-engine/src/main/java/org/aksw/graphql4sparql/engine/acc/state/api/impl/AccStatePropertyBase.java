package org.aksw.graphql4sparql.engine.acc.state.api.impl;

/**
 * Base class for property accumulators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AccStatePropertyBase<I, E, K, V>
    extends AccStateTransitionBase<I, E, K, V>
{
    /** The member key (=name) being aggregated. */
    protected K memberKey;
    /** Whether this is a single value. */
    protected boolean isSingle = false;

    /**
     * Creates a new AccStatePropertyBase.
     *
     * @param matchStateId The match state id
     * @param memberKey The member key
     * @param isSingle Whether this is a single value
     */
    protected AccStatePropertyBase(Object matchStateId, K memberKey, boolean isSingle) {
        super(matchStateId);
        this.matchStateId = matchStateId;
        this.memberKey = memberKey;
        this.isSingle = isSingle;
    }

    /**
     * Gets the member key.
     *
     * @return The member key
     */
    public K getMemberKey() {
        return memberKey;
    }

    /**
     * Checks if this is a single value.
     *
     * @return true if single, false otherwise
     */
    public boolean isSingle() {
        return isSingle;
    }
}
