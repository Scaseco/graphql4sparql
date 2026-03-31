package org.aksw.graphql4sparql.engine.acc.state.api;

import org.aksw.graphql4sparql.engine.io.RdfObjectNotationWriter;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.path.P_Path0;

/**
 * Context for RDF-based JSON accumulation operations.
 */
public class AccContextRdf
    extends AccContext<P_Path0, Node>
{
    /**
     * Creates a new AccContextRdf.
     *
     * @param writer The RdfObjectNotationWriter for streaming output
     * @param materialize Whether to accumulate a JsonElement
     * @param serialize Whether to stream to the jsonWriter
     */
    public AccContextRdf(RdfObjectNotationWriter writer, boolean materialize, boolean serialize) {
        super(writer, materialize, serialize);
    }

    @Override
    public RdfObjectNotationWriter getJsonWriter() {
        return (RdfObjectNotationWriter)writer;
    }
}
