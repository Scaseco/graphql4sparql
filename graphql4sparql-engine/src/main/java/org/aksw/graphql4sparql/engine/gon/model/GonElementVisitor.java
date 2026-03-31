package org.aksw.graphql4sparql.engine.gon.model;

/**
 * A visitor for gon elements.
 *
 * @param <K> The key type
 * @param <V> The value type
 * @param <T> The return type
 */
public interface GonElementVisitor<K, V, T> {
    /**
     * Visits a gon array.
     *
     * @param element The element
     * @return The result
     */
    T visit(GonArray<K, V> element);

    /**
     * Visits a gon object.
     *
     * @param element The element
     * @return The result
     */
    T visit(GonObject<K, V> element);

    /**
     * Visits a gon literal.
     *
     * @param element The element
     * @return The result
     */
    T visit(GonLiteral<K, V> element);

    /**
     * Visits a gon null.
     *
     * @param element The element
     * @return The result
     */
    T visit(GonNull<K, V> element);
}
