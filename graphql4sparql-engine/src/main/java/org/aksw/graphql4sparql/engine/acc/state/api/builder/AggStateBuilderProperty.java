package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateTypeProduceEntry;
import org.aksw.graphql4sparql.engine.acc.state.api.impl.ArrayMode;
import org.aksw.graphql4sparql.engine.agg.state.impl.AggStateProperty;

/**
 * Builder for property aggregators.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public class AggStateBuilderProperty<I, E, K, V>
    extends AggStateBuilderEdge<I, E, K, V>
{
    /** Whether the property is single-valued. */
    protected boolean isSingle = false;
    /** The key. */
    protected K key;
    /** The array mode. */
    protected ArrayMode arrayMode;

    /**
     * Creates a new AggStateBuilderProperty.
     *
     * @param matchStateId The match state ID
     * @param arrayMode The array mode
     */
    public AggStateBuilderProperty(Object matchStateId, ArrayMode arrayMode) {
        super(matchStateId);
        this.arrayMode = arrayMode;
    }

    /**
     * Creates a new AggStateBuilderProperty.
     *
     * @param <I> The input type
     * @param <E> The environment type
     * @param <K> The key type
     * @param <V> The value type
     * @param matchStateId The match state ID
     * @param key The key
     * @param arrayMode The array mode
     * @return The new AggStateBuilderProperty
     */
    public static <I, E, K, V> AggStateBuilderProperty<I, E, K, V> of(Object matchStateId, K key, ArrayMode arrayMode) {
        AggStateBuilderProperty<I, E, K, V> result = new AggStateBuilderProperty<>(matchStateId, arrayMode);
        result.setKey(key);
        return result;
    }

    /**
     * Sets the key.
     *
     * @param key The key to set
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Gets the key.
     *
     * @return The key
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets whether the property is single-valued.
     *
     * @return True if single-valued
     */
    public boolean isSingle() {
        return isSingle;
    }

    /**
     * Sets whether the property is single-valued.
     *
     * @param single True if single-valued
     */
    public void setSingle(boolean single) {
        this.isSingle = single;
    }

    @Override
    public AggStateTypeProduceEntry<I, E, K, V> newAggregator() {
        AggStateGon<I, E, K, V> targetAgg = targetNodeMapper.newAggregator();
        AggStateProperty<I, E, K, V> result = AggStateProperty.of(matchStateId, key, isSingle, arrayMode, targetAgg);
        return result;
    }
}
