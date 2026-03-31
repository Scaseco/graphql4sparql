package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTypeProduceEntry;
import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateMap;

/**
 * Builder for map aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderMap<I, E, K, V>
    extends AggStateBuilderEdge<I, E, K, V>
{
    /** The function to map input and environment to key. */
    protected BiFunction<I, E, ? extends K> inputToKeyMapper;
    /** The predicate to test if single-valued. */
    protected BiPredicate<I, E> testIfSingle;

    /**
     * Creates a new AggStateBuilderMap.
     *
     * @param matchStateId The match state ID
     * @param inputToKeyMapper The function to map input and environment to key
     * @param testIfSingle The predicate to test if single-valued
     */
    public AggStateBuilderMap(Object matchStateId, BiFunction<I, E, ? extends K> inputToKeyMapper, BiPredicate<I, E> testIfSingle) {
        super(matchStateId);
        this.inputToKeyMapper = inputToKeyMapper;
        this.testIfSingle = testIfSingle;
    }

    @Override
    public AggStateTypeProduceEntry<I, E, K, V> newAggregator() {
        AggStateGon<I, E, K, V> subAgg = this.targetNodeMapper.newAggregator();
        return AggStateMap.of(matchStateId, inputToKeyMapper, testIfSingle, subAgg);
    }
}
