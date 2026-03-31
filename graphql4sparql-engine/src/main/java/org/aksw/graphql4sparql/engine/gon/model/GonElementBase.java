package org.aksw.graphql4sparql.engine.gon.model;

/**
 * Base class for gon elements.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public abstract class GonElementBase<K, V>
    implements GonElement<K, V>
{
    /**
     * The parent link.
     */
    protected ParentLink<K, V> parentLink;

    /**
     * Creates a new gon element base.
     */
    public GonElementBase() {
        super();
    }

    /**
     * Returns the parent link.
     *
     * @return The parent link
     */
    @Override
    public ParentLink<K, V> getParent() {
        return parentLink;
    }

    /**
     * Sets the parent link.
     *
     * @param parentLink The parent link
     */
    protected void setParent(ParentLink<K, V> parentLink) {
        this.parentLink = parentLink;
    }
}
