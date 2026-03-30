package org.aksw.graphql4sparql.engine.acc.state.api;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A builder for a set of sub-selections.
 *
 * @param <K> The key type
 */
public class AggSelectionBuilder<K> {

    /** The parent builder. */
    protected final AggSelectionBuilder<K> parent;

    /** The sub-selections by name. */
    protected final Map<String, AggSelectionBuilder<K>> subSelectionsByName = new LinkedHashMap<>();
    /** The sub-selections by key. */
    protected final Map<K, AggSelectionBuilder<K>> subSelectionsByKey = new LinkedHashMap<>();

    /**
     * Creates a new AggSelectionBuilder.
     *
     * @param parent The parent builder
     */
    public AggSelectionBuilder(AggSelectionBuilder<K> parent) {
        super();
        this.parent = parent;
    }

    void newMapBuilder() {}

    /** Introduce a new field with a fixed name that does not introduce a new graph pattern. The parent's graph patterns variables are accessible from it. */
    void newHollow() {}

    /** Introduce a new field with a fixed name. */
    AggFieldBuilder newField() { return null; }

    /** Return an object that references a variable. Can be used to pass variables from ancestors to a builder. */
    void newVarRef(String varName) {}
}
