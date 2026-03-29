package org.aksw.graphql4sparql.engine.rewrite;

import org.aksw.graphql4sparql.engine.acc.state.api.builder.AggStateBuilder;
import org.aksw.graphql4sparql.engine.model.ElementNode;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.function.FunctionEnv;

/**
 * Intermediate state for generating the final SPARQL query.
 *
 * @param <K> The key type
 * @param rootElementNode The root element node
 * @param rootAggBuilder The root aggregation state builder
 * @param isSingle Whether this field produces a single result
 * @param graphQlNode The GraphQL node
 */
public record GraphQlFieldRewrite<K>(
    ElementNode rootElementNode,
    AggStateBuilder<Binding, FunctionEnv, K, Node> rootAggBuilder,
    boolean isSingle,
    graphql.language.Node<?> graphQlNode) {
}
