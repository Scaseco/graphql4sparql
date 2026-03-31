package org.aksw.graphql4sparql.engine.exec.api.low;

import java.util.LinkedHashMap;
import java.util.Map;

import org.aksw.graphql4sparql.engine.rewrite.GraphQlToSparqlConverterBase;
import org.aksw.graphql4sparql.engine.rewrite.GraphQlToSparqlConverterJson;
import org.aksw.graphql4sparql.engine.rewrite.GraphQlToSparqlConverterRon;
import org.aksw.graphql4sparql.engine.schema.SchemaNavigator;
import org.apache.jena.sparql.path.P_Path0;

import graphql.language.FragmentDefinition;

public abstract class GraphQlToSparqlConverterBuilder<K> {
    protected SchemaNavigator schemaNavigator;
    protected Map<String, FragmentDefinition> nameToFragment = new LinkedHashMap<>();

    public GraphQlToSparqlConverterBuilder<K> schemaNavigator(SchemaNavigator schemaNavigator) {
        this.schemaNavigator = schemaNavigator;
        return this;
    }

    public GraphQlToSparqlConverterBuilder<K> nameToFragment(Map<String, FragmentDefinition> nameToFragement) {
        this.nameToFragment = nameToFragement;
        return this;
    }

    public abstract GraphQlToSparqlConverterBase<K> build();

    public static GraphQlToSparqlConverterBuilder<String> forJson() {
        return new GraphQlToSparqlConverterBuilder<>() {
            @Override
            public GraphQlToSparqlConverterBase<String> build() {
                return new GraphQlToSparqlConverterJson(nameToFragment, schemaNavigator);
            }
        };
    }

    public static GraphQlToSparqlConverterBuilder<P_Path0> forRon() {
        return new GraphQlToSparqlConverterBuilder<>() {
            @Override
            public GraphQlToSparqlConverterBase<P_Path0> build() {
                return new GraphQlToSparqlConverterRon(nameToFragment, schemaNavigator);
            }
        };
    }
}
