package org.aksw.graphql4sparql.engine.api2;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.path.P_Link;
import org.apache.jena.sparql.path.P_Path0;
import org.apache.jena.sparql.path.P_ReverseLink;
import org.apache.jena.sparql.path.Path;

/**
 * Interface for things that can be traversed with SPARQL 1.1 property paths.
 *
 * @param <T> The type of the traversable
 */
public interface SparqlPathTraversable<T>
    extends RdfTraversable<T>
{
    /**
     * Takes a step with a node in forward or backward direction.
     *
     * @param node The node to step to
     * @param isForward Whether to step forward
     * @return The traversable object
     */
    @Override
    default T step(Node node, boolean isForward) {
        P_Path0 p0 = isForward ? new P_Link(node) : new P_ReverseLink(node);
        T result = step(p0);
        return result;
    }

    /**
     * Takes a step with a path.
     *
     * @param path The path to step with
     * @return The traversable object
     */
    T step(Path path);
}
