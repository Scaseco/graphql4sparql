## Example Fuseki Setup

1. Build the fuseki plugin jar by running on the project root folder: `make fuseki-mod`
2. Copy the built jar into Fuseki's `run/extra/` folder.

Once Fuseki is running, visit the graphql interface at: `http://localhost:3030/graphql-test/graphql`

Thanks to the schema, it is possible to write simple GraphQL queries such as the following.
Use `@pretty` for pretty formatting and `@debug` for the generated response to include the underlying SPARQL query string.

```graphql
query Works @pretty @debug {
  Work {
    type
    title
    creator
  }
}
```

```json
{
  "data": {
    "Work": [
      {
        "type": "http://dbpedia.org/ontology/Work",
        "title": "Mona Lisa",
        "creator": "http://dbpedia.org/resource/Leonardo_da_Vinci"
      }
    ]
  },
  "errors": []
  "extensions": {
    "metadata": {
      "sparqlQuery": "# SPARQL query string omitted for brevity."
    }
  }
}
```

GraphQl4Sparql also supports sophisticated patterns where GraphQL fields are turned into dynamic data generators.
The following is the generic "`SELECT SPO`" query which groups all data by subject, predicate and objects.

```graphql
query spo @debug @pretty
{
  subjects @pattern(of: "SELECT DISTINCT ?s { ?s ?p ?o } ", to: "s") @index(by: "?s", oneIf: "true") {
    predicates @pattern(of: "?s2 ?p2 ?o2", from: "s2", to: ["s2", "p2"]) @index(by: "?p2", oneIf: "false") @array {
      objects @pattern(of: "?s3 ?p3 ?o3", from: ["s3", "p3"], to: "o3")
    }
  }
}
```

Expected response:
```json
{
  "data": {
    "http://dbpedia.org/resource/Mona_Lisa": {
      "http://purl.org/dc/terms/creator": [
        "http://dbpedia.org/resource/Leonardo_da_Vinci"
      ],
      "http://purl.org/dc/terms/title": [
        "Mona Lisa"
      ],
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#type": [
        "http://dbpedia.org/ontology/Work"
      ]
    },
    "http://www.example.org/Bob": {
      "http://schema.org/birthDate": [
        "1990-07-04"
      ],
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#type": [
        "http://xmlns.com/foaf/0.1/Person"
      ],
      "http://xmlns.com/foaf/0.1/knows": [
        "http://www.example.org/Alice"
      ],
      "http://xmlns.com/foaf/0.1/topic_interest": [
        "http://dbpedia.org/resource/Mona_Lisa"
      ]
    }
  },
  "errors": [],
  "extensions": {
    "metadata": {
      "sparqlQuery": "# SPARQL query string omitted for brevity."
    }
  }
}
```

