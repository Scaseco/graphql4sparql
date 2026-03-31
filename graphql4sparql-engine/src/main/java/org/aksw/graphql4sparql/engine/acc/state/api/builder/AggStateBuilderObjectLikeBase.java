package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTransition;

/**
 * Base builder for object-like aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class AggStateBuilderObjectLikeBase<I, E, K, V>
    implements AggStateBuilder<I, E, K, V>
{
    // protected Map<K, AggStateBuilderEdge<I, E, K, V>> propertyMappers = new LinkedHashMap<>();
    /**
     * The edge mappers.
     */
    protected List<AggStateBuilderTransitionMatch<I, E, K, V>> edgeMappers = new ArrayList<>();
    // protected List<AggStateBuilder<I, E, K, V>> edgeMappers = new ArrayList<>();

    /**
     * Creates a new AggStateBuilderObjectLikeBase.
     */
    public AggStateBuilderObjectLikeBase() {
        super();
    }

    /**
     * Returns the property mappers.
     *
     * @return The property mappers
     */
    public List<AggStateBuilderTransitionMatch<I, E, K, V>> getPropertyMappers() {
        return edgeMappers;
    }

    /**
     * Adds a member to this builder.
     *
     * @param member The member to add
     */
    public void addMember(AggStateBuilderTransitionMatch<I, E, K, V> member) {
        Objects.requireNonNull(member);
        edgeMappers.add(member);
    }

    /**
     * Builds the sub-aggregators.
     *
     * @return The sub-aggregators array
     */
    public AggStateTransition<I, E, K, V>[] buildSubAggs() {
        int n = edgeMappers.size();
        @SuppressWarnings("unchecked")
        // AggStateTypeProduceEntry<I, E, K, V>[] subAggs = new AggStateTypeProduceEntry[n];
        AggStateTransition<I, E, K, V>[] subAggs = new AggStateTransition[n];
        int i = 0;

        for (AggStateBuilderTransitionMatch<I, E, K, V> entry : edgeMappers) {
        // for (AggStateBuilder<I, E, K, V> entry : edgeMappers) {
            subAggs[i] = entry.newAggregator();
            ++i;
        }
        //AggStateFragment<I, E, K, V> result = AggStateFragment.of(subAggs);
        return subAggs;
    }
}
