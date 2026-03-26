package org.aksw.graphql4sparql.engine.acc.state.api.builder;

public class AggStateBuilderTransitionWrapper<I, E, K, V, X extends AggStateBuilderTransition<I, E, K, V>>
    extends AggStateBuilderWrapper<I, E, K, V, X>
    implements AggStateBuilderTransition<I, E, K, V>
{
    public AggStateBuilderTransitionWrapper(X delegate) {
        super(delegate);
    }

    @Override
    public Object getMatchStateId() {
        return delegate.getMatchStateId();
    }
}
