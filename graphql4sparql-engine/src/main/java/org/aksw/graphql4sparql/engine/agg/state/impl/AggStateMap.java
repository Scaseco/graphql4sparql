package org.aksw.graphql4sparql.engine.agg.state.impl;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeProduceEntry;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AccStateMap;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTypeProduceEntry;

/**
 * Aggregator state for map-based aggregation.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateMap<I, E, K, V>
    implements AggStateTypeProduceEntry<I, E, K, V>
{
    /** The match state ID. */
    protected Object matchStateId;
    /** The function to map input and environment to key. */
    protected BiFunction<I, E, ? extends K> inputToKeyMapper;
    /** The predicate to test if single-valued. */
    protected BiPredicate<I, E> testIfSingle;
    /** The sub aggregator. */
    protected AggStateGon<I, E, K, V> subAgg;

    /**
     * Gets the match state ID.
     *
     * @return The match state ID
     */
    @Override
    public Object getMatchStateId() {
        return matchStateId;
    }

    /**
     * Gets the input to key mapper function.
     *
     * @return The input to key mapper function
     */
    public BiFunction<I, E, ? extends K> getInputToKeyMapper() {
        return inputToKeyMapper;
    }

    /**
     * Gets the test if single predicate.
     *
     * @return The test if single predicate
     */
    public BiPredicate<I, E> getTestIfSingle() {
        return testIfSingle;
    }

    /**
     * Gets the sub aggregator.
     *
     * @return The sub aggregator
     */
    public AggStateGon<I, E, K, V> getSubAgg() {
        return subAgg;
    }

    /**
     * Creates a new AggStateMap.
     *
     * @param matchStateId The match state ID
     * @param inputToKeyMapper The function to map input and environment to key
     * @param testIfSingle The predicate to test if single-valued
     * @param subAgg The sub aggregator
     */
    public AggStateMap(Object matchStateId, BiFunction<I, E, ? extends K> inputToKeyMapper, BiPredicate<I, E> testIfSingle, AggStateGon<I, E, K, V> subAgg) {
        super();
        this.matchStateId = matchStateId;
        this.inputToKeyMapper = inputToKeyMapper;
        this.testIfSingle = testIfSingle;
        this.subAgg = subAgg;
    }

    // sub agg must be of category produce node - AggStateTypeProduceNode
    /**
     * Creates a new AggStateMap.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchStateId The match state ID
     * @param inputToKeyMapper The function to map input and environment to key
     * @param testIfSingle The predicate to test if single-valued
     * @param subAgg The sub aggregator
     * @return The new AggStateMap
     */
    public static <I, E, K, V> AggStateMap<I, E, K, V> of(Object matchStateId, BiFunction<I, E, ? extends K> inputToKeyMapper, BiPredicate<I, E> testIfSingle, AggStateGon<I, E, K, V> subAgg) {
        return new AggStateMap<>(matchStateId, inputToKeyMapper, testIfSingle, subAgg);
    }

    @Override
    public AccStateTypeProduceEntry<I, E, K, V> newAccumulator() {
        AccStateGon<I, E, K, V> subAcc = subAgg.newAccumulator();
        AccStateMap<I, E, K, V> result = new AccStateMap<>(matchStateId, inputToKeyMapper, testIfSingle, subAcc);
        subAcc.setParent(result);
        return result;
    }

 }
