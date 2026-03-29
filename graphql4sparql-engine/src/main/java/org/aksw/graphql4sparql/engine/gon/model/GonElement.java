package org.aksw.graphql4sparql.engine.gon.model;

import org.aksw.graphql4sparql.engine.ron.RdfObject;

/**
 * A data model for RDF tree structures akin to gson's JsonElement.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface GonElement<K, V> {

    /**
     * Returns whether this element is an array.
     *
     * @return True if array, false otherwise
     */
    default boolean isArray() {
        return this instanceof GonArray;
    }

    /**
     * Returns this element as an array.
     *
     * @return This element as an array
     */
    default GonArray<K, V> getAsArray() {
        return (GonArray<K, V>)this;
    }

    /**
     * Returns whether this element is an object.
     *
     * @return True if object, false otherwise
     */
    default boolean isObject() {
        return this instanceof RdfObject;
    }

    /**
     * Returns this element as an object.
     *
     * @return This element as an object
     */
    default GonObject<K, V> getAsObject() {
        return (GonObject<K, V>)this;
    }

    /**
     * Returns whether this element is a literal.
     *
     * @return True if literal, false otherwise
     */
    default boolean isLiteral() {
        return this instanceof GonLiteral;
    }

    /**
     * Returns this element as a literal.
     *
     * @return This element as a literal
     */
    default GonLiteral<K, V> getAsLiteral() {
        return (GonLiteral<K, V>)this;
    }

    /**
     * Returns whether this element is null.
     *
     * @return True if null, false otherwise
     */
    default boolean isNull() {
        return this instanceof GonNull;
    }

    /**
     * Returns this element as a null.
     *
     * @return This element as a null
     */
    default GonNull<K, V> asNull() {
        return (GonNull<K, V>)this;
    }

    /**
     * Accepts a visitor.
     *
     * @param <T> The return type
     * @param visitor The visitor
     * @return The result
     */
    <T> T accept(GonElementVisitor<K, V, T> visitor);

    /**
     * Returns the parent link.
     *
     * @return The parent link
     */
    ParentLink<K, V> getParent();

    /**
     * Unlinks this element from its parent.
     */
    default void unlinkFromParent() {
        ParentLink<K, V> link = getParent();

        if (link != null) {
            if (link.isObjectLink()) {
                ParentLinkObject<K, V> objLink = link.asObjectLink();
                K key = objLink.getKey();
                objLink.getParent().remove(key);
            } else if (link.isArrayLink()) {
                ParentLinkArray<K, V> arrLink = link.asArrayLink();
                int index = arrLink.getIndex();
                // objLink.getParent().remove(index);
                arrLink.getParent().set(index, new GonNull<>());
            } else {
                throw new RuntimeException("Unknown parent link type: " + link.getClass());
            }
        } else {
            // Ignore
            // throw new RuntimeException("Cannot unlink an element that does not have a parent");
        }
    }

    /**
     * Returns the root element.
     *
     * @return The root element
     */
    default GonElement<K, V> getRoot() {
        ParentLink<K, V> link = getParent();
        GonElement<K, V> result = link == null ? this : link.getParent().getRoot();
        return result;
    }


//    public static RdfElement of(Node node) {
//        RdfElement result = node == null
//            ? RdfNull.get()
//            : RdfElement.newObject(node);
//        return result;
//    }

//    public static RdfElement newObject(Node node) {
//        return new RdfObjectImpl(node);
//    }
//
//    public static RdfArray newArray() {
//        return new RdfArrayImpl();
//    }
//
//    public static RdfElement newLiteral(Node node) {
//        return new RdfLiteralImpl(node);
//    }
//
//    public static RdfElement nullValue() {
//        return RdfNull.get();
//    }
}
