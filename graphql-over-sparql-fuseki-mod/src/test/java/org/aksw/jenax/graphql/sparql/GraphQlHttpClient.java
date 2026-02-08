package org.aksw.jenax.graphql.sparql;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class GraphQlHttpClient {
    public static JsonObject query(String graphqlServiceUrl, String graphqlQueryStr) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // GraphQL JSON payload: { "query": "...", "variables": { ... } }
        Map<String, Object> payload = Map.of("query", graphqlQueryStr, "variables", Map.of());

        String body = gson.toJson(payload);

        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(graphqlServiceUrl))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();

        HttpResponse<String> resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

        // System.out.println("HTTP " + resp.statusCode());

        JsonObject json = gson.fromJson(resp.body(), JsonObject.class);
        return json;
    }
}
