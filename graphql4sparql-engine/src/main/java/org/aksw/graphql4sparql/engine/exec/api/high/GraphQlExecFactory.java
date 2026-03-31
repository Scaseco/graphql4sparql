package org.aksw.graphql4sparql.engine.exec.api.high;

import java.util.Objects;

import org.aksw.graphql4sparql.engine.schema.SchemaNavigator;
import org.apache.jena.atlas.lib.Creator;
import org.apache.jena.sparql.exec.QueryExecBuilder;

public class GraphQlExecFactory {
    protected Creator<QueryExecBuilder> queryExecBuilderFactory;
    protected SchemaNavigator schemaNavigator;

    public GraphQlExecFactory(Creator<QueryExecBuilder> queryExecBuilderFactory, SchemaNavigator schemaNavigator) {
        super();
        this.queryExecBuilderFactory = Objects.requireNonNull(queryExecBuilderFactory);
        this.schemaNavigator = schemaNavigator;
    }

    public static GraphQlExecFactory of(Creator<QueryExecBuilder> queryExecBuilderFactory) {
        return of(queryExecBuilderFactory, null);
    }

    public static GraphQlExecFactory of(Creator<QueryExecBuilder> queryExecBuilderFactory, SchemaNavigator schemaNavigator) {
        return new GraphQlExecFactory(queryExecBuilderFactory, schemaNavigator);
    }

    public GraphQlExecBuilder newBuilder() {
        return new GraphQlExecBuilder()
            .queryExecBuilderFactory(queryExecBuilderFactory)
            .schemaNavigator(schemaNavigator);
    }
}
