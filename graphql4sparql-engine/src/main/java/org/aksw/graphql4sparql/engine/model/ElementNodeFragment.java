package org.aksw.graphql4sparql.engine.model;

import java.util.List;

import org.aksw.graphql4sparql.engine.rewrite.GraphQlFieldRewrite;
import org.apache.jena.sparql.core.Var;


// This class is essentially a rewrite result with a list of default variables to connect on
public record ElementNodeFragment<K>(GraphQlFieldRewrite<K> rewrite, List<Var> defaultConnectVars) {
    public ElementNodeFragment {
        // XXX Validate that the connect variables are visible in elementNode
    }
}
