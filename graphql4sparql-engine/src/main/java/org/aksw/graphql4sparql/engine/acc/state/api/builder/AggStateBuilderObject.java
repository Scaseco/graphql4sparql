package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;
import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateObject;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Builder for object aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderObject<I, E, K, V>
    extends AggStateBuilderObjectLikeBase<I, E, K, V>
    implements AggStateBuilderNode<I, E, K, V>
{
     /** Whether the object is an array. */
    protected boolean isArray;

    /**
     * Creates a new AggStateBuilderObject.
     *
     * @param isArray Whether the object is an array
     */
    public AggStateBuilderObject(boolean isArray) {
        super();
        this.isArray = isArray;
    }

    /**
     * Returns whether the object is an array.
     *
     * @return True if array, false otherwise
     */
    public boolean isArray() {
        return isArray;
    }

    @Override
    public GonType getGonType() {
        return GonType.OBJECT;
    }

    @Override
    public String toString() {
        return "AggStateBuilderObject [propertyMappers=" + edgeMappers + "]";
    }

    @Override
    public AggStateObject<I, E, K, V> newAggregator() {
        AggStateTransition<I, E, K, V>[] subAggs = buildSubAggs();
        AggStateObject<I, E, K, V> result = AggStateObject.of(isArray, subAggs);
        return result;
    }
}
