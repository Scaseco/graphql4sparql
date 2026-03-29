package org.aksw.graphql4sparql.engine.gon.model;

/**
 * A literal simply wraps a node. Unlike RdfObject, it cannot have additional properties.
 * It thus represents a leaf in a tree structure.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class GonLiteralImpl<K, V>
    extends GonElementBase<K, V>
    implements GonLiteral<K, V>
{
    /**
     * The value.
     */
    // XXX final?
    protected V value;

    /**
     * Creates a new gon literal implementation.
     *
     * @param value The value
     */
    public GonLiteralImpl(V value) {
        super();
        this.value = value;
    }

    /**
     * Returns the value.
     *
     * @return The value
     */
    @Override
    public V getValue() {
        return value;
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
