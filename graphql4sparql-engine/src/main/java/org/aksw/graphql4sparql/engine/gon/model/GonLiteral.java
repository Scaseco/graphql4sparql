package org.aksw.graphql4sparql.engine.gon.model;

public interface GonLiteral<K, V>
    extends GonElement<K, V>
{
    V getValue();
}
