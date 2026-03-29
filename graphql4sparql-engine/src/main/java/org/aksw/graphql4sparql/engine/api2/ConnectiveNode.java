package org.aksw.graphql4sparql.engine.api2;

/**
 * A node in the connective structure.
 */
public interface ConnectiveNode {
    /**
     * Accepts a visitor and returns a result.
     *
     * @param <T> The return type
     * @param visitor The visitor to accept
     * @return The result from the visitor
     */
    <T> T accept(ConnectiveVisitor<T> visitor);
}
