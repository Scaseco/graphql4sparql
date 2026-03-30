package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Wrapper class for AggStateBuilder implementations.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 * @param <X> The delegate type
 */
public class AggStateBuilderWrapper<I, E, K, V, X extends AggStateBuilder<I, E, K, V>>
    implements AggStateBuilder<I, E, K, V>
{
    /**
     * The delegate builder.
     */
    protected X delegate;

    /**
     * Creates a new AggStateBuilderWrapper.
     *
     * @param delegate The delegate builder
     */
    public AggStateBuilderWrapper(X delegate) {
        super();
        this.delegate = delegate;
    }

    /**
     * Gets the gon type from the delegate.
     *
     * @return The gon type
     */
    @Override
    public GonType getGonType() {
        return delegate.getGonType();
    }

    /**
     * Creates a new aggregator from the delegate.
     *
     * @return The new aggregator
     */
    @Override
    public AggStateGon<I, E, K, V> newAggregator() {
        return delegate.newAggregator();
    }
}
