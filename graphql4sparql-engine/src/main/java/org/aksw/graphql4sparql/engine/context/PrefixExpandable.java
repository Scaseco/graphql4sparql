package org.aksw.graphql4sparql.engine.context;

import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.graph.PrefixMappingAdapter;

/**
 * Interface for objects that can be expanded with prefix mappings.
 *
 * @param <T> The type of the expandable object
 */
public interface PrefixExpandable<T> {

    /**
     * Expands the object with the given prefix map.
     *
     * @param prefixMap The prefix map to use for expansion
     * @return The expanded object
     */
    default T expand(PrefixMap prefixMap) {
        PrefixMapping pm = new PrefixMappingAdapter(prefixMap);
        T result = expand(pm);
        return result;
    }

    /**
     * Expands the object with the given prefix mapping.
     *
     * @param pm The prefix mapping to use for expansion
     * @return The expanded object
     */
    T expand(PrefixMapping pm);
}
