package org.aksw.graphql4sparql.engine.acc.state.api;


/** Type for accumulators that accept values (arrays as items or fields as values).
 * I.e. accumulators that do not produce objects. */
public interface AccStateTypeNonObject<I, E, K, V>
    extends AccStateGon<I, E, K, V>
{
}
