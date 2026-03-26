package org.aksw.graphql4sparql.engine.gon.model;

public abstract class GonElementBase<K, V>
    implements GonElement<K, V>
{
    protected ParentLink<K, V> parentLink;

    public GonElementBase() {
        super();
    }

    @Override
    public ParentLink<K, V> getParent() {
        return parentLink;
    }

    protected void setParent(ParentLink<K, V> parentLink) {
        this.parentLink = parentLink;
    }
}
