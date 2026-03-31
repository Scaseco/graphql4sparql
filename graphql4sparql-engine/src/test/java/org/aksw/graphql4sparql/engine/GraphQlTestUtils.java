package org.aksw.graphql4sparql.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.aksw.graphql4sparql.engine.exec.api.low.GraphQlFieldExec;
import org.aksw.graphql4sparql.engine.exec.api.low.RdfGraphQlProcessorFactoryImpl;
import org.aksw.graphql4sparql.engine.gon.model.GonProviderGson;
import org.aksw.graphql4sparql.engine.io.GraphQlIoBridge;
import org.aksw.graphql4sparql.engine.ron.RdfElement;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.exec.QueryExec;
import org.apache.jena.sparql.path.P_Path0;

public class GraphQlTestUtils {
    private static Gson gson = new Gson();

    public static void doAssertJson(DatasetGraph dataset, String documentStr, String expectedResult) {
        JsonElement expected = gson.fromJson(expectedResult, JsonElement.class);

        JsonElement actual = null;
        try (GraphQlFieldExec<String> qe = RdfGraphQlProcessorFactoryImpl.forJson().newBuilder()
                .document(documentStr)
                // set mode?
            .build() // or have buildForJson and buildForRdf here?
            // .getFieldProcessor(1).newExecBuilder()
            .newExecBuilder()
            .service(() -> QueryExec.dataset(dataset))
            .build()) {

        // GraphQlExecUtils.write(System.out, qe);
            // qe.sendNextItemToWriter(GraphQlIoBridge.

            Iterator<JsonElement> it = qe.asIterator(GraphQlIoBridge.bridgeToJsonInMemory(GonProviderGson.of()));
            while (it.hasNext()) {
                actual = it.next();
            }
        }

        assertEquals(expected, actual);
    }

    public static void doAssertRon(DatasetGraph dataset, String documentStr, String expectedResult) {
        JsonElement expected = gson.fromJson(expectedResult, JsonElement.class);

        RdfElement actual = null;
        try (GraphQlFieldExec<P_Path0> qe = RdfGraphQlProcessorFactoryImpl.forRon().newBuilder()
                .document(documentStr)
                // set mode?
            .build() // or have buildForJson and buildForRdf here?
            // .getFieldProcessor(1).newExecBuilder()
            .newExecBuilder()
            .service(() -> QueryExec.dataset(dataset))
            .build()) {

        // GraphQlExecUtils.write(System.out, qe);
            // qe.sendNextItemToWriter(GraphQlIoBridge.

            Iterator<RdfElement> it = qe.asIterator(GraphQlIoBridge.bridgeRonToRdfElement());
            while (it.hasNext()) {
                actual = it.next();
            }
        }

        assertEquals(expected, actual);
    }


    public static void doAssertJsonWithRon(DatasetGraph dataset, String documentStr, String expectedResult) {
        JsonElement expected = gson.fromJson(expectedResult, JsonElement.class);

        JsonElement actual = null;
        try (GraphQlFieldExec<P_Path0> qe = RdfGraphQlProcessorFactoryImpl.forRon().newBuilder()
                .document(documentStr)
                // set mode?
            .build() // or have buildForJson and buildForRdf here?
            // .getFieldProcessor(1).newExecBuilder()
            .newExecBuilder()
            .service(() -> QueryExec.dataset(dataset))
            .build()) {

        // GraphQlExecUtils.write(System.out, qe);
            // qe.sendNextItemToWriter(GraphQlIoBridge.

            Iterator<JsonElement> it = qe.asIterator(GraphQlIoBridge.bridgeRonToJsonInMemory(GonProviderGson.of()));
            while (it.hasNext()) {
                actual = it.next();
            }
        }

        assertEquals(expected, actual);
    }
}
