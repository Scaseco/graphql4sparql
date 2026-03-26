package org.aksw.graphql4sparql.engine.api2;

public interface HasSelf<T> {
    @SuppressWarnings("unchecked")
    default T self() {
        return (T)this;
    }
}
