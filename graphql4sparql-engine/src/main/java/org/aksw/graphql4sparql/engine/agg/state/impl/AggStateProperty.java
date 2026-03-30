package org.aksw.graphql4sparql.engine.agg.state.impl;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceEntry;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AccStateProperty;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStatePropertyBase;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.ArrayMode;

/**
 * Aggregator state for a preset member key.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateProperty<I, E, K, V>
    extends AggStatePropertyBase<I, E, K, V>
{
    /** The member key being aggregated */
    /** The aggregator for the value */
    protected AggStateGon<I, E, K, V> subAgg;
    // protected AggStateTypeProduceNode<I, E, K, V> subAgg;

    /**
     * Creates a new AggStateProperty.
     *
     * @param matchStateId The match state ID
     * @param memberKey The member key
     * @param isSingle Whether the property is single-valued
     * @param arrayMode The array mode
     * @param subAgg The sub aggregator
     */
    protected AggStateProperty(Object matchStateId, K memberKey, boolean isSingle, ArrayMode arrayMode, AggStateGon<I, E, K, V> subAgg) {
        super(matchStateId, memberKey, isSingle, arrayMode);
        this.subAgg = subAgg;
    }

    /**
     * Creates a new AggStateProperty with many values.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchFieldId The match field ID
     * @param memberKey The member key
     * @param subAgg The sub aggregator
     * @return The new AggStateProperty
     */
    public static <I, E, K, V> AggStateProperty<I, E, K, V> many(Object matchFieldId, K memberKey, AggStateGon<I, E, K, V> subAgg) {
        return of(matchFieldId, memberKey, false, ArrayMode.OFF, subAgg);
    }

    /**
     * Creates a new AggStateProperty with one value.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchFieldId The match field ID
     * @param memberKey The member key
     * @param subAgg The sub aggregator
     * @return The new AggStateProperty
     */
    public static <I, E, K, V> AggStateProperty<I, E, K, V> one(Object matchFieldId, K memberKey, AggStateGon<I, E, K, V> subAgg) {
        return of(matchFieldId, memberKey, true, ArrayMode.OFF, subAgg);
    }

    // AggStateTypeProduceNode
    /**
     * Creates a new AggStateProperty.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchFieldId The match field ID
     * @param memberKey The member key
     * @param isSingle Whether the property is single-valued
     * @param arrayMode The array mode
     * @param subAgg The sub aggregator
     * @return The new AggStateProperty
     */
    public static <I, E, K, V> AggStateProperty<I, E, K, V> of(Object matchFieldId, K memberKey, boolean isSingle, ArrayMode arrayMode, AggStateGon<I, E, K, V> subAgg) {
        return new AggStateProperty<>(matchFieldId, memberKey, isSingle, arrayMode, subAgg);
    }

    @Override
    public AccStateTypeProduceEntry<I, E, K, V> newAccumulator() {
        AccStateGon<I, E, K, V> valueAcc = subAgg.newAccumulator();
        AccStateProperty<I, E, K, V> result = new AccStateProperty<>(matchStateId, memberKey, valueAcc, isSingle, arrayMode);
        valueAcc.setParent(result);
        return result;
    }
}
