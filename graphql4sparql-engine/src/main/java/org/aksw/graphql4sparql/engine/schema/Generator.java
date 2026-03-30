package org.aksw.graphql4sparql.engine.schema;

/**
 * A generator for producing sequences of items.
 *
 * @param <T> The type of items
 */
public interface Generator<T> {
    /**
     * Returns the next item in the sequence.
     *
     * @return The next item
     */
    T next();

    /**
     * Returns the current item in the sequence.
     *
     * @return The current item
     */
    T current();

    /**
     * Clones should independently yield the same sequences of items as the original object.
     *
     * @return The cloned generator
     */
    Generator<T> clone(); // throws CloneNotSupportedException;
}
