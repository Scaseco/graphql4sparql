package org.aksw.graphql4sparql.engine.acc.state.api;

import org.aksw.graphql4sparql.engine.io.RdfObjectNotationWriter;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.path.P_Path0;

public class AccContextRdf
    extends AccContext<P_Path0, Node>
{
    public AccContextRdf(RdfObjectNotationWriter writer, boolean materialize, boolean serialize) {
        super(writer, materialize, serialize);
    }

    @Override
    public RdfObjectNotationWriter getJsonWriter() {
        return (RdfObjectNotationWriter)writer;
    }
}
