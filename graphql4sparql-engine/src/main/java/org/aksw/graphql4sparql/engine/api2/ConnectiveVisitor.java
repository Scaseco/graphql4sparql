package org.aksw.graphql4sparql.engine.api2;

import org.aksw.graphql4sparql.engine.model.ElementNode;

public interface ConnectiveVisitor<T> {
    T visit(ElementNode field);
    T visit(FragmentSpread fragmentSpread);

    T visit(Connective connective);
    // T visit(Fragment connective);

    // T visit(SelectionSet selectionSet);
}
