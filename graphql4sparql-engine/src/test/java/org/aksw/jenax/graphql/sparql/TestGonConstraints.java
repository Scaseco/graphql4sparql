package org.aksw.jenax.graphql.sparql;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.aksw.jenax.graphql.sparql.v2.gon.meta.GonType;

public class TestGonConstraints {
    @Test
    public void test() {
        // An array can be seen as both a non-object and a node-type.
        // assertTrue(GonCategory.NODE_TYPE.getMembers().contains(GonType.ARRAY.getRawType()));

        assertTrue(GonType.ARRAY.isValidChildOf(GonType.ARRAY));
        assertTrue(GonType.ARRAY.isValidChildOf(GonType.ENTRY));
        assertFalse(GonType.ARRAY.isValidChildOf(GonType.LITERAL));
        assertFalse(GonType.ARRAY.isValidChildOf(GonType.OBJECT));
        assertTrue(GonType.ARRAY.isValidChildOf(GonType.ROOT));
        assertTrue(GonType.ARRAY.isValidChildOf(GonType.UNKNOWN));

        assertFalse(GonType.ENTRY.isValidChildOf(GonType.ARRAY));
        assertFalse(GonType.ENTRY.isValidChildOf(GonType.ENTRY));
        assertFalse(GonType.ENTRY.isValidChildOf(GonType.LITERAL));
        assertTrue(GonType.ENTRY.isValidChildOf(GonType.OBJECT));
        assertFalse(GonType.ENTRY.isValidChildOf(GonType.ROOT));
        assertTrue(GonType.ENTRY.isValidChildOf(GonType.UNKNOWN));

        assertTrue(GonType.LITERAL.isValidChildOf(GonType.ARRAY));
        assertTrue(GonType.LITERAL.isValidChildOf(GonType.ENTRY));
        assertFalse(GonType.LITERAL.isValidChildOf(GonType.LITERAL));
        assertFalse(GonType.LITERAL.isValidChildOf(GonType.OBJECT));
        assertTrue(GonType.LITERAL.isValidChildOf(GonType.ROOT));
        assertTrue(GonType.LITERAL.isValidChildOf(GonType.UNKNOWN));

        assertTrue(GonType.OBJECT.isValidChildOf(GonType.ARRAY));
        assertTrue(GonType.OBJECT.isValidChildOf(GonType.ENTRY));
        assertFalse(GonType.OBJECT.isValidChildOf(GonType.LITERAL));
        assertFalse(GonType.OBJECT.isValidChildOf(GonType.OBJECT));
        assertTrue(GonType.OBJECT.isValidChildOf(GonType.ROOT));
        assertTrue(GonType.OBJECT.isValidChildOf(GonType.UNKNOWN));
    }
}
