package org.aksw.graphql4sparql.engine.schema;

public abstract class SchemaNodeBase
    implements SchemaNode
{
    protected SchemaNavigator navigator;

    public SchemaNodeBase(SchemaNavigator navigator) {
        super();
        this.navigator = navigator;
    }

    public SchemaNavigator getNavigator() {
        return navigator;
    }
}
