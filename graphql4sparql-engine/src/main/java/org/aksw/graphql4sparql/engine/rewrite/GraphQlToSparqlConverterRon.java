package org.aksw.graphql4sparql.engine.rewrite;

import java.util.Map;

import org.aksw.graphql4sparql.engine.schema.SchemaNavigator;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.path.P_Link;
import org.apache.jena.sparql.path.P_Path0;

import graphql.language.Field;
import graphql.language.FragmentDefinition;

public class GraphQlToSparqlConverterRon
    extends GraphQlToSparqlConverterBase<P_Path0>
{
    public GraphQlToSparqlConverterRon(Map<String, FragmentDefinition> nameToFragment,
            SchemaNavigator schemaNavigator) {
        super(nameToFragment, schemaNavigator);
    }

    @Override
    protected P_Path0 toKey(Field field) {
        P_Path0 result = XGraphQlUtils.fieldToRonKey(field);
        return result;
    }

    @Override
    protected P_Path0 toKey(Node node) {
        return new P_Link(node);
    }
}
