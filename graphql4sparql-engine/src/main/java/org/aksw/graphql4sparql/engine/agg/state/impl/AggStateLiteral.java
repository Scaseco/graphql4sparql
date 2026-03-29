package org.aksw.graphql4sparql.engine.agg.state.impl;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceNode;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AccStateLiteral;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTypeProduceNode;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Aggregator state for literal values.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateLiteral<I, E, K, V>
    implements AggStateTypeProduceNode<I, E, K, V>
{
    /** The function to map input and environment to value. */
    protected BiFunction<I, E, ? extends V> inputToValue;

    /**
     * Gets the input to value function.
     *
     * @return The input to value function
     */
    public BiFunction<I, E, ? extends V> getInputToValue() {
        return inputToValue;
    }

    /**
     * Creates a new AggStateLiteral.
     *
     * @param inputToValue The function to map input and environment to value
     */
    protected AggStateLiteral(BiFunction<I, E, ? extends V> inputToValue) {
        super();
        this.inputToValue = inputToValue;
    }

    /**
     * Creates a new AggStateLiteral.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param inputToValue The function to map input and environment to value
     * @return The new AggStateLiteral
     */
    public static <I, E, K, V> AggStateLiteral<I, E, K, V> of(BiFunction<I, E, ? extends V> inputToValue) {
        return new AggStateLiteral<>(inputToValue);
    }

    /**
     * Creates a new AggStateLiteral.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param keyClz The key class
     * @param inputToValue The function to map input and environment to value
     * @return The new AggStateLiteral
     */
    public static <I, E, K, V> AggStateLiteral<I, E, K, V> of(Class<K> keyClz, BiFunction<I, E, ? extends V> inputToValue) {
        return new AggStateLiteral<>(inputToValue);
    }

    /**
     * Creates a new AggStateLiteral with a composite function.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param <X> The temporary value type
     * @param inputToTmp The function to map input and environment to temporary value
     * @param tmpToValue The function to map temporary value to final value
     * @return The new AggStateLiteral
     */
    public static <I, E, K, V, X> AggStateLiteral<I, E, K, V> of(BiFunction<I, E, X> inputToTmp, Function<? super X, ? extends V> tmpToValue) {
        BiFunction<I, E, V> composite = inputToTmp.andThen(tmpToValue);
        return of(composite);
    }

    @Override
    public GonType getGonType() {
        return GonType.LITERAL;
    }

    @Override
    public AccStateTypeProduceNode<I, E, K, V> newAccumulator() {
        return AccStateLiteral.of(inputToValue);
    }
}
