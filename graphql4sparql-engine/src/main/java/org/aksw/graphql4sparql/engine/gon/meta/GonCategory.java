package org.aksw.graphql4sparql.engine.gon.meta;

import java.util.EnumSet;

/**
 * Categories for gon elements.
 */
public enum GonCategory {
    /**
     * Non-object category.
     */
    NON_OBJECT(EnumSet.of(RawGonType.ARRAY, RawGonType.ENTRY, RawGonType.ROOT)),
    /**
     * Node type category.
     */
    NODE_TYPE (EnumSet.of(RawGonType.LITERAL, RawGonType.OBJECT, RawGonType.ARRAY)),
    /**
     * Object category.
     */
    OBJECT    (EnumSet.of(RawGonType.OBJECT)), // The object and node type categories contain OBJECT
    /**
     * Root category.
     */
    ROOT      (EnumSet.of(RawGonType.ROOT)),

    /**
     * Unknown category.
     */
    UNKNOWN   (EnumSet.allOf(RawGonType.class)),
    ;

    /**
     * The members.
     */
    private final EnumSet<RawGonType> members;

    /**
     * Creates a new gon category.
     *
     * @param members The members
     */
    private GonCategory(EnumSet<RawGonType> members) {
        this.members = members;
    }

    /**
     * Returns the members.
     *
     * @return The members
     */
    public EnumSet<RawGonType> getMembers() {
        return members;
    }
}
