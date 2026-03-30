package org.aksw.graphql4sparql.engine.api2;

import java.util.List;

import org.apache.jena.sparql.core.Var;

/**
 * A builder for selections.
 */
public interface SelectionBuilder {
    /**
     * Returns the name of this selection.
     *
     * @return The name
     */
    String getName();

    /**
     * Returns the base name of this selection.
     *
     * @return The base name
     */
    String getBaseName();

    /**
     * Returns the connective of this selection.
     *
     * @return The connective
     */
    Connective getConnective();

    /**
     * Returns the parent variables.
     *
     * @return The parent variables
     */
    List<Var> getParentVars();

    /**
     * Create a copy of the builder with different name for the selection. Needed to have the parent allocate a name.
     * FIXME Seems hacky.
     *
     * @param finalName The final name
     * @param parentVars The parent variables
     * @return The cloned selection builder
     */
    SelectionBuilder clone(String finalName, List<Var> parentVars);

    // Name must be configurable
    // FieldLikeBuilder setName(String name);

    /**
     * Builds the selection.
     *
     * @return The built selection
     */
    Selection build();
}
