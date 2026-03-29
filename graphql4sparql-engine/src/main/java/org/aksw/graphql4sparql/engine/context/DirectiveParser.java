package org.aksw.graphql4sparql.engine.context;

import graphql.language.Directive;

/**
 * A parser for GraphQL directives.
 *
 * @param <T> The type of the parsed result
 */
public interface DirectiveParser<T> {
    /**
     * Returns the name of the directive this parser parses.
     *
     * @return The directive name
     */
    String getName();

    /**
     * Returns whether the directive is unique.
     *
     * @return True if the directive is unique, false otherwise
     */
    boolean isUnique();

    /**
     * Parses a directive.
     *
     * @param directive The directive to parse
     * @return The parsed result
     */
    T parser(Directive directive);
}
