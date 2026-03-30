package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import java.util.function.BiFunction;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.ArrayMode;
import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateLiteralProperty;

/**
 * Literal properties can suppress emitting null values.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderLiteralProperty<I, E, K, V>
    extends AggStateBuilderTransitionMatch<I, E, K, V>
{
     /** The function to map input and environment to value. */
    protected BiFunction<I, E, ? extends V> inputToValue;
    /** Whether to skip null values. */
    protected boolean skipIfNull;
    /** Whether the property is single-valued. */
    protected boolean isSingle = false;
    /** The array mode. */
    protected ArrayMode arrayMode;
    /** The key. */
    protected K key;

    /**
     * Creates a new AggStateBuilderLiteralProperty.
     *
     * @param matchStateId The match state ID
     */
    public AggStateBuilderLiteralProperty(Object matchStateId) {
        super(matchStateId);
    }

    /**
     * Creates a new AggStateBuilderLiteralProperty.
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
     * @return The new AggStateBuilderLiteralProperty
     */
    public static <I, E, K, V> AggStateBuilderLiteralProperty<I, E, K, V> of(Object matchStateId, K key, boolean isSingle, boolean skipIfNull, ArrayMode arrayMode, BiFunction<I, E, ? extends V> inputToValue) {
        AggStateBuilderLiteralProperty<I, E, K, V> result = new AggStateBuilderLiteralProperty<>(matchStateId);
        result.key = key;
        result.isSingle = isSingle;
        result.skipIfNull = skipIfNull;
        result.inputToValue = inputToValue;
        result.arrayMode = arrayMode;
        return result;
    }

    @Override
    public AggStateTransition<I, E, K, V> newAggregator() {
        return AggStateLiteralProperty.of(matchStateId, key, isSingle, skipIfNull, arrayMode, inputToValue);
    }
}
