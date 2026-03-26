package org.aksw.graphql4sparql.engine.util;

import org.apache.jena.graph.Node;

public class NodeUtils {
    public static boolean isNullOrAny(Node node) {
        return node == null || Node.ANY.equals(node);
    }
}
