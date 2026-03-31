package org.aksw.graphql4sparql.engine.acc.state.api;

/**
 * Represents the cardinality of a result set.
 */
public enum Cardinality {
    /**
     * Exactly one result.
     */
    ONE,
    /**
     * Zero or more results.
     */
    MANY
}
