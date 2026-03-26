package org.aksw.graphql4sparql.engine.exec.api.low;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.aksw.graphql4sparql.engine.schema.SchemaNavigator;
import org.apache.jena.graph.Node;

import graphql.language.Document;
import graphql.language.FragmentDefinition;

public abstract class RdfGraphQlProcessorBuilderBase<K>
    implements RdfGraphQlProcessorBuilder<K>
{
    protected Document document;
    protected Map<String, Node> assignments = new LinkedHashMap<>(); // Binding?
    // protected SchemaNavigator schemaNavigator;
    protected GraphQlToSparqlConverterBuilder<K> converterBuilder;

    public RdfGraphQlProcessorBuilderBase(GraphQlToSparqlConverterBuilder<K> converterBuilder) {
        super();
        this.converterBuilder = Objects.requireNonNull(converterBuilder);
    }

    @Override
    public RdfGraphQlProcessorBuilder<K> document(Document document) {
        this.document = document;
        return this;
    }

    @Override
    public RdfGraphQlProcessorBuilder<K> setVar(String name, Node value) {
        this.assignments.put(name, value);
        return this;
    }

    @Override
    public RdfGraphQlProcessorBuilder<K> schemaNavigator(SchemaNavigator schemaNavigator) {
        converterBuilder.schemaNavigator(schemaNavigator);
        return this;
    }

    @Override
    public RdfGraphQlProcessorBuilder<K> nameToFragment(Map<String, FragmentDefinition> nameToFragment) {
        converterBuilder.nameToFragment(nameToFragment);
        return this;
    }
}
