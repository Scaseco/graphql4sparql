package org.aksw.jenax.graphql.sparql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.aksw.jenax.fuseki.mod.graphql.ServerUtils;
import org.aksw.jenax.web.servlet.graphql.GraphQlUi;
import org.apache.jena.fuseki.main.FusekiMain;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.vocabulary.RDF;

// @Disabled("Test must build or unpack a Fuseki config")
public class TestGraphQLFuseki {
    private DatasetGraph dsg;
    private FusekiServer server;

    @BeforeEach
    public void setUp() {
        dsg = DatasetGraphFactory.create();
        setupTestData(dsg);

        String graphQlUiResource = ServerUtils.getExternalForm(GraphQlUi.class);
        // System.out.println(getExternalForm(GraphQlUi.class));

//        if (true) {
//            throw new RuntimeException("Aborted.");
//        }
//        // Variante 1: Die Files via mehrerer servlets einklinken (jedes file ein servlet)
//        // Variante 2:
//        HttpServlet serv = null;

        System.setProperty("FUSEKI_BASE", "/home/raven/Repositories/coypu/fuseki-with-jenax/run/configuration");

        Model configModel = RDFParser.fromString("""
            PREFIX xdt:       <http://jsa.aksw.org/dt/sparql/>
            PREFIX rdf:       <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX rdfs:      <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX owl:       <http://www.w3.org/2002/07/owl#>
            PREFIX tdb1:      <http://jena.hpl.hp.com/2008/tdb#>
            PREFIX tdb2:      <http://jena.apache.org/2016/tdb#>
            PREFIX text:      <http://jena.apache.org/text#>
            PREFIX ja:        <http://jena.hpl.hp.com/2005/11/Assembler#>
            PREFIX geosparql: <http://jena.apache.org/geosparql#>
            PREFIX fuseki:    <http://jena.apache.org/fuseki#>
            PREFIX norse:     <https://w3id.org/aksw/norse#>

            <#service> rdf:type fuseki:Service ;
              fuseki:name "graphql-test" ;
              fuseki:dataset <#ds1> ;
              fuseki:endpoint [
                fuseki:name "graphql" ;
                fuseki:operation norse:graphql.fmod.op ;
                # ja:context [ ja:cxtName "https://w3id.org/aksw/norse#graphql.schemaFile" ; ja:cxtValue "/run/configuration/mobydex.graphql" ];
                # ja:context [ ja:cxtName "https://w3id.org/aksw/norse#graphql.schemaFile"          ; ja:cxtValue "/run/configuration/coypu.graphql" ] ;
                ja:context [ ja:cxtName "https://w3id.org/aksw/norse#graphql.sparqlQueryEndpoint" ; ja:cxtValue "/mobydex" ] ;
                ja:context [ ja:cxtName "https://w3id.org/aksw/norse#graphql.sparqlQueryViewer"   ; ja:cxtValue "https://yasgui.triply.cc/#?query={ENCODED_SPARQL_QUERY}&endpoint={ENCODED_SPARQL_QUERY_ENDPOINT}" ] ;
                # ja:context [ ja:cxtName "https://w3id.org/aksw/norse#graphql.sparqlQueryViewer"   ; ja:cxtValue "/#/dataset/coypu/query?query={ENCODED_SPARQL_QUERY}" ] ;
              ] ;
              fuseki:endpoint [
                fuseki:operation fuseki:query ;
              ] ;
              fuseki:endpoint [
                fuseki:name "update" ;
                fuseki:operation fuseki:update ;
              ] ;
              fuseki:endpoint [
                fuseki:operation fuseki:gsp_rw ;
                fuseki:name "data" ;
                fuseki:allowedUsers "katherine" ;
              ] ;
              .

              <#ds1> a ja:MemoryDataset .
            """, Lang.TURTLE).toModel();

        String[] argv = new String[] { "--empty" };
        server = FusekiMain.builder(argv)
            .parseConfig(configModel)
            // .parseConfigFile("/home/raven/Repositories/coypu/fuseki-with-jenax/run/config.ttl")
            // .addServlet("graphql.bundle.js", serv)
            // .staticFileBase("/home/raven/Projects/Eclipse/jenax/jenax-graphql-parent/jenax-graphql-v2-parent/jenax-graphql-v2-ui/frontend/build")
            // .staticFileBase(Path.of("").toAbsolutePath().toString())
            .add("test", dsg)
            .build();
        server.start();
        int port = server.getPort();
        String serverURL = "http://localhost:" + port + "/";
        String graphqlUrl = serverURL + "test/graphql";
        System.out.println(graphqlUrl);
    }

    private void setupTestData(DatasetGraph dsg) {
        dsg.getDefaultGraph().add(RDF.Nodes.type, RDF.Nodes.type, RDF.Nodes.type);
//        // Fill the graph with a few geometries; spatial index construction will derive the SRS from them.
//        Envelope envelope = new Envelope(-175, 175, -85, 85);
//
//        Map<GeometryType, Number> conf = new HashMap<>();
//        conf.put(GeometryType.POINT, 1);
//
//        // Generate geometries into the default graph and a named graph
//        GeometryGenerator.generateGraph(dsg.getDefaultGraph(), envelope, conf);
//
//        conf.put(GeometryType.POLYGON, 1);
//        GeometryGenerator.generateGraph(dsg.getGraph(graphName1), envelope, conf);
    }

    @AfterEach
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void test() throws InterruptedException {
         // Thread.sleep(100000);
    }
}
