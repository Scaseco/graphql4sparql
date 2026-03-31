package org.aksw.graphql4sparql.engine.agg.state.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.aksw.graphql4sparql.engine.acc.state.api.AccStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.AccStateTypeTransition;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;

/**
 * Base class for aggregator states with member sets.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStateMemberSet<I, E, K, V>
    implements AggStateGon<I, E, K, V>
{
    /**
     * The member aggregators map.
     */
    protected Map<Object, AggStateTransition<I, E, K, V>> memberAggs = new LinkedHashMap<>();

    /**
     * Creates a new AggStateMemberSet.
     */
    public AggStateMemberSet() {
        super();
    }

    /**
     * Gets the property aggregators.
     *
     * @return The property aggregators map
     */
    public Map<Object, AggStateTransition<I, E, K, V>> getPropertyAggregators() {
        return memberAggs;
    }

    /**
     * Adds a property aggregator.
     *
     * @param propertyAggregator The property aggregator to add
     */
    protected void addPropertyAggregator(AggStateTransition<I, E, K, V> propertyAggregator) {
        // XXX Validate that there is no clash in member keys
        Object matchStateId = propertyAggregator.getMatchStateId();
        memberAggs.put(matchStateId, propertyAggregator);
    }

    @Override
    public abstract AccStateGon<I, E, K, V> newAccumulator();

    /**
     * Record holding member accumulator information.
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param fieldIdToIndex Map from field ID to index
     * @param edgeAccs Array of edge accumulators
     */
    public static record MemberAccs<I, E, K, V>(Map<Object, Integer> fieldIdToIndex, AccStateTypeTransition<I, E, K, V>[] edgeAccs) {}

    /**
     * Builds the member accumators.
     *
     * @return The member accumators
     */
    public MemberAccs<I, E, K, V> buildMemberAccs() {
        int n = memberAggs.size();

        Map<Object, Integer> fieldIdToIndex = new HashMap<>();
        @SuppressWarnings("unchecked")
        AccStateTypeTransition<I, E, K, V>[] edgeAccs = new AccStateTypeTransition[n];

        int fieldIndex = 0;
        for (Entry<Object, AggStateTransition<I, E, K, V>> e : memberAggs.entrySet()) {
            Object matchStateId = e.getKey();
            AggStateTransition<I, E, K, V> agg = e.getValue();

            AccStateTypeTransition<I, E, K, V> acc = agg.newAccumulator();

            fieldIdToIndex.put(matchStateId, fieldIndex);
            edgeAccs[fieldIndex] = acc;
            ++fieldIndex;
        }

        return new MemberAccs<>(fieldIdToIndex, edgeAccs);
    }
}
