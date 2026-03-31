package org.aksw.graphql4sparql.fuseki.mod.graphql;

public abstract class ResourceSourceBase
    implements ResourceSource
{
    protected String contentType;

    public ResourceSourceBase(String contentType) {
        super();
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
