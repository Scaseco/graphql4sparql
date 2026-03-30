package org.aksw.graphql4sparql.engine.acc.state.api;

import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Accumulator that produces entries, i.e. (key, value) pairs.
 * The keys emitted by an AccStateTypeEntry can be dynamic - both in value and amount.
 * From the perspective of an ObjectNotation writer, this method invokes the
 * ".name(key)" method.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AccStateTypeProduceEntry<I, E, K, V>
    extends AccStateTypeTransition<I, E, K, V>
    // extends AccStateGon<I, E, K, V>
//    extends AccStateTypeNonObject<I, E, K, V>
{
    @Override
    default GonType getGonType() {
        return GonType.ENTRY;
    }

    /**
     * Gets the match state id.
     *
     * @return The match state id
     */
    Object getMatchStateId();
}
