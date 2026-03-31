package org.aksw.graphql4sparql.engine.gon.model;

public interface ParentLinkObject<K, V>
    extends ParentLink<K, V>
{
    @Override
    GonObject<K, V> getParent();

    K getKey();
}
