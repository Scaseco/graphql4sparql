package org.aksw.graphql4sparql.engine.gon.model;

/**
 * A null element.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class GonNull<K, V>
    extends GonElementBase<K, V>
{
    /**
     * Creates a new gon null.
     */
    public GonNull() {
        super();
    }

    /**
     * Accepts a visitor.
     *
     * @param visitor The visitor
     * @return The result
     */
    @Override
    public <T> T accept(GonElementVisitor<K, V, T> visitor) {
        T result = visitor.visit(this);
        return result;
    }
}
