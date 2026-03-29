package org.aksw.graphql4sparql.engine.context;

import java.util.Objects;

/**
 * A base class for cascading directives.
 */
public class Cascadable {
    /**
     * Whether this is a self reference.
     */
    protected boolean isSelf;

    /**
     * Whether to cascade.
     */
    protected boolean isCascade;

    /**
     * Creates a new Cascadable instance.
     *
     * @param isSelf Whether this is a self reference
     * @param isCascade Whether to cascade
     */
    public Cascadable(boolean isSelf, boolean isCascade) {
        super();
        this.isSelf = isSelf;
        this.isCascade = isCascade;
    }

    /**
     * Returns whether this is a self reference.
     *
     * @return True if this is a self reference, false otherwise
     */
    public boolean isSelf() {
        return isSelf;
    }

    /**
     * Returns whether to cascade.
     *
     * @return True if cascading is enabled, false otherwise
     */
    public boolean isCascade() {
        return isCascade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCascade, isSelf);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cascadable other = (Cascadable) obj;
        return isCascade == other.isCascade && isSelf == other.isSelf;
    }

    @Override
    public String toString() {
        return "Cascadable [isSelf=" + isSelf + ", isCascade=" + isCascade + "]";
    }
}
