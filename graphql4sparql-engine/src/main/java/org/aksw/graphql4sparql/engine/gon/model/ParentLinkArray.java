package org.aksw.graphql4sparql.engine.gon.model;

public interface ParentLinkArray<K, V>
    extends ParentLink<K, V>
{
    @Override
    GonArray<K, V> getParent();

    int getIndex();
}
