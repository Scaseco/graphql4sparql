package org.aksw.graphql4sparql.engine.acc.state.api;

/**
 * AccState that produces literals, objects or arrays - but not entries.
 * The parent must produce ENTRY or ARRAY (but not object).
 *
 * @param <I> The input type
 * @param <E> The environment type
 * @param <K> The key type
 * @param <V> The value type
 */
public interface AccStateTypeProduceNode<I, E, K, V>
    extends AccStateGon<I, E, K, V>
{
//    @Override
//    AccStateTypeNonObject<I, E, K, V> getParent();
}
