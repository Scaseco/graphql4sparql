package org.aksw.graphql4sparql.engine.api2;

import org.aksw.graphql4sparql.engine.model.ElementNode;

/**
 * Visitor interface for connective nodes.
 *
 * @param <T> The return type of the visit methods
 */
public interface ConnectiveVisitor<T> {
    /**
     * Visits an element node.
     *
     * @param field The element node to visit
     * @return The result of the visit
     */
    T visit(ElementNode field);

    /**
     * Visits a fragment spread.
     *
     * @param fragmentSpread The fragment spread to visit
     * @return The result of the visit
     */
    T visit(FragmentSpread fragmentSpread);

    /**
     * Visits a connective.
     *
     * @param connective The connective to visit
     * @return The result of the visit
     */
    T visit(Connective connective);
    // T visit(Fragment connective);

    // T visit(SelectionSet selectionSet);
}
