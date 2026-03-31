package org.aksw.graphql4sparql.engine.ron;

public interface ParentLink {
    RdfElement getParent();

    default boolean isObjectLink() {
        return this instanceof ParentLinkObject;
    }

    default ParentLinkObject asObjectLink() {
        return (ParentLinkObject)this;
    }

    default boolean isArrayLink() {
        return this instanceof ParentLinkArray;
    }

    default ParentLinkArray asArrayLink() {
        return (ParentLinkArray)this;
    }
}