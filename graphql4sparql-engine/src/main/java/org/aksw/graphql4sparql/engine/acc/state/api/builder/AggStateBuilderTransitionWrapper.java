package org.aksw.graphql4sparql.engine.acc.state.api.builder;

/**
 * Wrapper class for AggStateBuilderTransition implementations.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 * @param <X> The delegate type
 */
public class AggStateBuilderTransitionWrapper<I, E, K, V, X extends AggStateBuilderTransition<I, E, K, V>>
    extends AggStateBuilderWrapper<I, E, K, V, X>
    implements AggStateBuilderTransition<I, E, K, V>
{
    /**
     * The delegate builder.
     */
    protected X delegate;

    /**
     * Creates a new AggStateBuilderTransitionWrapper.
     *
     * @param delegate The delegate builder
     */
    public AggStateBuilderTransitionWrapper(X delegate) {
        super(delegate);
    }

    /**
     * Gets the match state ID from the delegate.
     *
     * @return The match state ID
     */
    @Override
    public Object getMatchStateId() {
        return delegate.getMatchStateId();
    }
}
