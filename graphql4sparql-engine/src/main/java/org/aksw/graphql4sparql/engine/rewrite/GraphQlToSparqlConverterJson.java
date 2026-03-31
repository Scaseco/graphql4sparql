package org.aksw.graphql4sparql.engine.rewrite;

import java.util.Map;

import org.aksw.graphql4sparql.engine.io.GraphQlIoBridge;
import org.aksw.graphql4sparql.engine.schema.SchemaNavigator;
import org.apache.jena.graph.Node;

import graphql.language.Field;
import graphql.language.FragmentDefinition;

public class GraphQlToSparqlConverterJson
    extends GraphQlToSparqlConverterBase<String>
{
    public GraphQlToSparqlConverterJson(Map<String, FragmentDefinition> nameToFragment,
            SchemaNavigator schemaNavigator) {
        super(nameToFragment, schemaNavigator);
    }

    @Override
    protected String toKey(Field field) {
        String result = XGraphQlUtils.fieldToJsonKey(field);
        return result;
    }

    @Override
    protected String toKey(Node node) {
        String result = GraphQlIoBridge.getPlainString(node);
        return result;
    }
}
