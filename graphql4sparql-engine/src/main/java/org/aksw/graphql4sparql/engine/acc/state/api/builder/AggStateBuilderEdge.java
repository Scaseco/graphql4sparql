package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Base builder for edge aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStateBuilderEdge<I, E, K, V>
    extends AggStateBuilderTransitionBase<I, E, K, V>
{
    /**
     * Only applicable if the value produced by this PropertyMapper is a json object.
     * If hidden is true, then the owning NodeMapper should merge the produced json object into
     * its own json object.
     */
    protected boolean isHidden = false;

    /**
     * Creates a new AggStateBuilderEdge.
     *
     * @param matchStateId The match state ID
     */
    public AggStateBuilderEdge(Object matchStateId) {
        super(matchStateId);
    }

    @Override
    public GonType getGonType() {
        return GonType.ENTRY;
    }

    // public abstract AggStateBuilderNode<I, E, K, V> getTargetNodeMapper();

    /**
     * Returns whether the edge is hidden.
     *
     * @return True if hidden, false otherwise
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Sets whether the edge is hidden.
     *
     * @param isHidden True to hide the edge, false otherwise
     */
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
}
