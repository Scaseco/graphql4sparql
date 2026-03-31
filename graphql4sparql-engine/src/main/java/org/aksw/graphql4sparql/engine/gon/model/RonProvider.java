package org.aksw.graphql4sparql.engine.gon.model;

import org.apache.jena.graph.Node;

/**
 * Abstraction akin to JSONPath's JsonProvider.
 * There are subtle differences between JSON and RON: In RON, keys and literals are all RDF terms.
 */
public interface RonProvider {
    // Should we require the id of an object to be set in advance?
    Object newObject(Node node);
    boolean isObject(Object obj);

    /** Get the id of an object.
     *  Can be thought of as extracting the {@code @id} attribute of a JSON-LD object. */
    Node getObjectId(Object obj);
    void setProperty(Object obj, Node p, boolean isFoward, Object value);
    Object getProperty(Object obj, Node p, boolean isForward);
    void removeProperty(Object obj, Node p, boolean isForward);

    Object newArray();
    boolean isArray(Object obj);
    void addElement(Object arr, Object value);
    void setElement(Object arr, int index, Object value);
    void removeElement(Object arr, int index);

    Object newLiteral(Node node);
    boolean isLiteral(Object obj);
    Node getLiteral(Object obj);

    Object newNull();
    boolean isNull(Object obj);
}
