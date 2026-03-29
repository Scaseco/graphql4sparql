package org.aksw.graphql4sparql.engine.gon.model;

/**
 * A literal element.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface GonLiteral<K, V>
    extends GonElement<K, V>
{
    /**
     * Returns the value.
     *
     * @return The value
     */
    V getValue();
}
