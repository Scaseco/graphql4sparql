package org.aksw.graphql4sparql.engine.gon.model;

/**
 * A generic array interface.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface GonArray<K, V>
    extends GonElement<K, V>, Iterable<GonElement<K, V>>
{
    /**
     * Returns the size of the array.
     *
     * @return The size
     */
    int size();

    /**
     * Returns the element at the given index.
     *
     * @param index The index
     * @return The element
     */
    GonElement<K, V> get(int index);

    /**
     * Adds an element to the array.
     *
     * @param element The element to add
     * @return This array
     */
    GonArray<K, V> add(GonElement<K, V> element);

    /**
     * Sets the element at the given index.
     *
     * @param index The index
     * @param element The element
     * @return This array
     */
    GonArray<K, V> set(int index, GonElement<K, V> element);

    /**
     * Removes the element at the given index.
     *
     * @param index The index
     * @return This array
     */
    GonArray<K, V> remove(int index);
}
