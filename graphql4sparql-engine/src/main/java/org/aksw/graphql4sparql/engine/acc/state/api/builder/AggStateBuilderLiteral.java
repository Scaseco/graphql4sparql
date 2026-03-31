package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import java.util.function.BiFunction;

import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateLiteral;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Builder for literal value aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderLiteral<I, E, K, V>
    implements AggStateBuilder<I, E, K, V>
{
    // private static final AggStateBuilderLiteral INSTANCE = new AggStateBuilderLiteral();
    // public static <I, E, K, V> AggStateBuilderLiteral<I, E, K, V> get() {
    //    return (AggStateBuilderLiteral<I, E, K, V>)INSTANCE;
    // }

      /** The function to map input and environment to value. */
    protected BiFunction<I, E, ? extends V> inputToValue;

    /**
     * Creates a new AggStateBuilderLiteral with the given input to value function.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param inputToValue The function to map input and environment to value
     * @return The new AggStateBuilderLiteral
     */
    public static <I, E, K, V> AggStateBuilderLiteral<I, E, K, V> of(BiFunction<I, E, ? extends V> inputToValue) {
        AggStateBuilderLiteral<I, E, K, V> result = new AggStateBuilderLiteral<>();
        result.setInputToValue(inputToValue);
        return result;
    }

    /**
     * Sets the input to value function.
     *
     * @param inputToValue The function to map input and environment to value
     * @return This builder instance
     */
    public AggStateBuilderLiteral<I, E, K, V> setInputToValue(BiFunction<I, E, ? extends V> inputToValue) {
        this.inputToValue = inputToValue;
        return this;
    }

    /**
     * Returns the input to value function.
     *
     * @return The input to value function
     */
    public BiFunction<I, E, ? extends V> getInputToValue() {
        return inputToValue;
    }

    /**
     * Creates a new AggStateBuilderLiteral.
     */
    protected AggStateBuilderLiteral() {
        super();
    }

    @Override
    public GonType getGonType() {
        return GonType.LITERAL;
    }

    @Override
    public String toString() {
        return "NodeMapperLiteral []";
    }

    @Override
    public AggStateLiteral<I, E, K, V> newAggregator() {
        return AggStateLiteral.of(inputToValue);
    }
}
