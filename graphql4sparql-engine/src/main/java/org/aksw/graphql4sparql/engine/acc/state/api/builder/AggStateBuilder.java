package org.aksw.graphql4sparql.engine.acc.state.api.builder;

import org.aksw.graphql4sparql.engine.acc.state.api.impl.AggStateGon;
import org.aksw.graphql4sparql.engine.gon.meta.GonType;

/**
 * Builder interface for aggregator states.
 *
 * <ul>
 *   <li>{@linkplain AggStateBuilderLiteral}: Only used with &commat;index as a leaf value. Emit a literal value.</li>
 *   <li>{@linkplain AggStateBuilderObject}: Emits an object with start and end markers whenever a given expression evaluates to a new id.</li>
 *   <li>{@linkplain AggStateBuilderProperty}: (possibly multi-valued, has targetNodeMapper) Produces a single JSON entry - emits a key. Does not emit object start/end markers. If the property is multi-valued, then the value will always be an array.</li>
 *   <li>{@linkplain AggStateBuilderLiteralProperty}: Emit an entry, possibly array-valued. May omit output if the value is null.</li>
 *   <li>{@linkplain AggStateBuilderMap}: Produces a set of JSON entries. Does not emit object start/end markers.</li>
 *   <li>{@linkplain AggStateBuilderFragmentHead}: Condition whether to enter a state with a {@linkplain AggStateBuilderFragmentBody} processor.</li>
 *   <li>{@linkplain AggStateBuilderFragmentBody}: Emits a set of fields but does not emit start and end marker for objects - in contrast to {@linkplain AggStateBuilderObject}.</li>
 * </ul>
 *
 * The part below is wip:
 * When turning a field into an Array then the parent becomes an AggStateBuilderArray, and the following changes apply to the children:
 *
 *   We still need to match the transition, but we just don't need to emit the entry-key. So the only difference is whether to emit the key.
 *   In this case, a flag would be easier to use than a type hierarchy.
 *
 *   AggStateBuilderProperty -> use its value.
 *   AggStateBuilderLiteralProperty -> use its value.
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AggStateBuilder<I, E, K, V> {
    /**
     * Returns the GON type.
     *
     * @return The GON type
     */
    GonType getGonType();

    /**
     * Creates a new aggregator.
     *
     * @return The new aggregator
     */
    AggStateGon<I, E, K, V> newAggregator();
}
