package org.aksw.graphql4sparql.engine.api2;

public interface ConnectiveNode {
    <T> T accept(ConnectiveVisitor<T> visitor);
}
