package org.aksw.graphql4sparql.engine.acc.state.api;

import org.aksw.graphql4sparql.engine.api2.Connective;

/**
 * Builder for field aggregators.
 *
 * @param <K> The key type
 */
public class AggFieldBuilder<K> {
    /**
     * The object builder for which this builder builds a member.
     */
    // protected AggObjectBuilder<K> parent;

    /**
     * The key of the resulting object for which this builder builds an aggregator. Fixed by the parent.
     */
    protected K fieldName;

    /**
     * The graph pattern that underlies the built aggregator.
     */
    protected Connective connective;

    /**
     * Whether values of this member should be accumulated into an array.
     */
    protected Cardinality cardinality;

    /**
     * If hidden, then the values produced by this field should go to the parent.
     */
    protected boolean isHidden;

    /**
     * Creates a new AggFieldBuilder.
     */
    public AggFieldBuilder() {
        super();
    }
}
