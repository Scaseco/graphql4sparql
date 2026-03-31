package org.aksw.graphql4sparql.engine.context;

import java.util.Objects;

/**
 * A directive that specifies the cardinality of a result.
 */
public class CardinalityDirective extends Cascadable {
    /**
     * Whether the cardinality is exactly one.
     */
    protected boolean isOne;

    /**
     * Creates a new CardinalityDirective.
     *
     * @param isOne Whether the cardinality is exactly one
     * @param cascadable The cascadable parent
     */
    public CardinalityDirective(boolean isOne, Cascadable cascadable) {
        this(isOne, cascadable.isSelf(), cascadable.isCascade());
    }

    /**
     * Creates a new CardinalityDirective.
     *
     * @param isOne Whether the cardinality is exactly one
     * @param isSelf Whether this is a self reference
     * @param isCascade Whether to cascade
     */
    public CardinalityDirective(boolean isOne, boolean isSelf, boolean isCascade) {
        super(isSelf, isCascade);
        this.isOne = isOne;
    }

    /**
     * Returns whether the cardinality is exactly one.
     *
     * @return True if cardinality is ONE, false otherwise
     */
    public boolean isOne() {
        return isOne;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(isOne);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CardinalityDirective other = (CardinalityDirective) obj;
        return isOne == other.isOne;
    }

    @Override
    public String toString() {
        return "CardinalityDirective [isOne=" + isOne + ", toString()=" + super.toString() + "]";
    }
}
