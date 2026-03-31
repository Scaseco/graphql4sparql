package org.aksw.graphql4sparql.engine.model;

import java.util.List;

import org.aksw.graphql4sparql.engine.rewrite.GraphQlFieldRewrite;
import org.apache.jena.sparql.core.Var;


/**
 * A fragment of an ElementNode after rewriting.
 *
 * @param <K> The key type for the rewrite
 * @param rewrite The graph query rewrite
 * @param defaultConnectVars The default variables to connect on
 */
public record ElementNodeFragment<K>(GraphQlFieldRewrite<K> rewrite, List<Var> defaultConnectVars) {
    /**
     * Creates a new ElementNodeFragment.
     *
     * @param rewrite The graph query rewrite
     * @param defaultConnectVars The default variables to connect on
     */
    public ElementNodeFragment {
        // XXX Validate that the connect variables are visible in elementNode
    }
}
