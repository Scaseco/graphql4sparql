package org.aksw.graphql4sparql.engine.agg.state.impl;

import java.util.function.BiFunction;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceEntry;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AccStateLiteralProperty;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStatePropertyBase;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.ArrayMode;

/**
 * Aggregator state for literal properties.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateLiteralProperty<I, E, K, V>
    extends AggStatePropertyBase<I, E, K, V>
{
    /** Whether to skip null values. */
    protected boolean skipIfNull;
    /** The function to map input and environment to value. */
    protected BiFunction<I, E, ? extends V> inputToValue;

    /**
     * Gets whether to skip null values.
     *
     * @return True if null values should be skipped
     */
    public boolean isSkipIfNull() {
        return skipIfNull;
    }

    /**
     * Gets the input to value function.
     *
     * @return The input to value function
     */
    public BiFunction<I, E, ? extends V> getInputToValue() {
        return inputToValue;
    }

    /**
     * Creates a new AggStateLiteralProperty.
     *
     * @param matchStateId The match state ID
     * @param key The key
     * @param isSingle Whether the property is single-valued
     * @param skipIfNull Whether to skip null values
     * @param arrayMode The array mode
     * @param inputToValue The function to map input and environment to value
     */
    protected AggStateLiteralProperty(Object matchStateId, K key, boolean isSingle, boolean skipIfNull, ArrayMode arrayMode, BiFunction<I, E, ? extends V> inputToValue) {
        super(matchStateId, key, isSingle, arrayMode);
        this.skipIfNull = skipIfNull;
        this.inputToValue = inputToValue;
    }

    /**
     * Creates a new AggStateLiteralProperty.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchStateId The match state ID
     * @param key The key
     * @param isSingle Whether the property is single-valued
     * @param skipIfNull Whether to skip null values
     * @param arrayMode The array mode
     * @param inputToValue The function to map input and environment to value
     * @return The new AggStateLiteralProperty
     */
    public static <I, E, K, V> AggStateLiteralProperty<I, E, K, V> of(Object matchStateId, K key, boolean isSingle, boolean skipIfNull, ArrayMode arrayMode, BiFunction<I, E, ? extends V> inputToValue) {
        return new AggStateLiteralProperty<>(matchStateId, key, isSingle, skipIfNull, arrayMode, inputToValue);
    }

    @Override
    public AccStateTypeProduceEntry<I, E, K, V> newAccumulator() {
        return AccStateLiteralProperty.of(matchStateId, memberKey, isSingle, skipIfNull, arrayMode, inputToValue);
    }
}
