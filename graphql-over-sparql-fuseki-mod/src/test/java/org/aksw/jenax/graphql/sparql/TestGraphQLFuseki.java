package org.aksw.jenax.graphql.sparql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.aksw.jenax.fuseki.mod.graphql.ServerUtils;
import org.aksw.jenax.graphql.sparql.v2.exec.api.high.GraphQlExec;
import org.aksw.jenax.graphql.sparql.v2.exec.api.high.GraphQlExecFactory;
import org.aksw.jenax.graphql.sparql.v2.gon.model.GonProviderGson;
import org.aksw.jenax.graphql.sparql.v2.io.GraphQlIoBridge;
import org.aksw.jenax.graphql.sparql.v2.io.ObjectNotationWriterInMemory;
import org.aksw.jenax.web.servlet.graphql.GraphQlUi;
import org.apache.jena.fuseki.main.FusekiMain;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModules;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.exec.QueryExec;
import org.apache.jena.sparql.exec.UpdateExec;

// @Disabled("Test must build or unpack a Fuseki config")
public class TestGraphQLFuseki {
    private DatasetGraph dsg;
    private FusekiServer server;

    // TODO Include error key!
    public static JsonElement materialize(GraphQlExecFactory gef, String documentStr) throws IOException {
        ObjectNotationWriterInMemory<JsonElement, String, Node> writer = GraphQlIoBridge
                .bridgeToJsonInMemory(GonProviderGson.of());
        try (GraphQlExec<String> qe = gef.newBuilder().document(documentStr).buildForJson()) {
            qe.sendRemainingItemsToWriter(writer);
        }
        JsonElement result = writer.getProduct();
        return result;
    }

    @BeforeEach
    public void setUp() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String graphQlUiResource = ServerUtils.getExternalForm(GraphQlUi.class);
        // System.out.println(getExternalForm(GraphQlUi.class));

//        if (true) {
//            throw new RuntimeException("Aborted.");
//        }
//        // Variante 1: Die Files via mehrerer servlets einklinken (jedes file ein servlet)
//        // Variante 2:
//        HttpServlet serv = null;

        // System.setProperty("FUSEKI_BASE",
        // "/home/raven/Repositories/coypu/fuseki-with-jenax/run/configuration");

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
            """,
            Lang.TURTLE).toModel();

        String[] argv = new String[] { "--empty" };
        server = FusekiMain.builder(FusekiModules.getSystemModules(), argv).parseConfig(configModel)
                // .parseConfigFile("/home/raven/Repositories/coypu/fuseki-with-jenax/run/config.ttl")
                // .addServlet("graphql.bundle.js", serv)
                // .staticFileBase("/home/raven/Projects/Eclipse/jenax/jenax-graphql-parent/jenax-graphql-v2-parent/jenax-graphql-v2-ui/frontend/build")
                // .staticFileBase(Path.of("").toAbsolutePath().toString())
                .add("test", dsg).build();
        server.start();
        int port = server.getPort();
        String serverUrl = "http://localhost:" + port + "/";
        String sparqlQueryUrl = serverUrl + "graphql-test";
        String sparqlUpdateUrl = serverUrl + "graphql-test/update";
        String graphqlUrl = serverUrl + "graphql-test/graphql";

        System.out.println(sparqlQueryUrl);
        // System.out.println(graphqlUrl);
        JsonElement expectedData = gson.fromJson("""
            {
              "http://www.example.org/s": {
                "http://www.example.org/p": {
                  "objects": ["http://www.example.org/o"]
                }
              }
            }
            """, JsonElement.class);

        UpdateExec.service(sparqlUpdateUrl)
            .update("PREFIX eg: <http://www.example.org/> INSERT DATA { eg:s eg:p eg:o }")
            .execute();

        GraphQlExecFactory qef = GraphQlExecFactory.of(() -> QueryExec.service(sparqlQueryUrl));
        String queryStr = """
            query moviesSPO @debug @pretty
              @prefix(map: {
                wd: "http://www.wikidata.org/entity/"
                wdt: "http://www.wikidata.org/prop/direct/"
              })
            {
              subjects(limit: 10) @pattern(of: "SELECT DISTINCT ?s1 {?s1 ?p ?o }", to: "s1") @index(by: "?s1", oneIf: "true") {
                predicates @pattern(of: "?s2 ?p2 ?o2", from: "s2", to: ["s2", "p2"]) @index(by: "?p2", oneIf: "true") {
                  objects @pattern(of: "?s3 ?p3 ?o3", from: ["s3", "p3"], to: "o3")
                }
              }
            }
            """;

        JsonElement actualDataFromSparqlEndpoint = materialize(qef, queryStr);
        assertEquals(expectedData, actualDataFromSparqlEndpoint);

        JsonElement actualDataFromGraphQlEndpoint = GraphQlHttpClient.query(graphqlUrl, queryStr).get("data");
        assertEquals(expectedData, actualDataFromGraphQlEndpoint);
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
