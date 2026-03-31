package org.aksw.graphql4sparql.engine.ron;

public interface ParentLinkArray
    extends ParentLink
{
    @Override
    RdfArray getParent();

    int getIndex();
}
