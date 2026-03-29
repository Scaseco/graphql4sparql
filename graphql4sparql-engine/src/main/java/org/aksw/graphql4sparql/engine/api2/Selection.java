package org.aksw.graphql4sparql.engine.api2;

/**
 * Base type for individual members of selection set.
 * Concrete sub classes are Field and FragmentSpread.
 */
public interface Selection
    extends ConnectiveNode
{
    /**
     * Returns the name of this selection.
     *
     * @return The name
     */
    String getName();
    // SelectionSet getParent();

    /** Create a builder from the state of this selection. */
    // FieldLikeBuilder toBuilder(SelectionSetBuilder<?> selectionSetBuilder);
}
