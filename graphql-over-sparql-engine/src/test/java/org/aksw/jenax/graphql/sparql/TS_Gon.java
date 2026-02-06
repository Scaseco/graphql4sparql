package org.aksw.jenax.graphql.sparql;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import org.aksw.jenax.graphql.sparql.v2.gon.model.GonProvider;

public abstract class TS_Gon<K, V> {
    protected GonProvider<K, V> provider;

    public TS_Gon(GonProvider<K, V> provider) {
        super();
        this.provider = Objects.requireNonNull(provider);
    }

    @Test
    public void testObject() {
        Object obj = provider.newObject();
        assertTrue(provider.isObject(obj));
        assertFalse(provider.isArray(obj));
        assertFalse(provider.isLiteral(obj));
        assertFalse(provider.isNull(obj));
    }

    @Test
    public void testArray() {
        Object arr = provider.newArray();
        assertFalse(provider.isObject(arr));
        assertTrue(provider.isArray(arr));
        assertFalse(provider.isLiteral(arr));
        assertFalse(provider.isNull(arr));
    }

    @Test
    public void testNull() {
        Object nil = provider.newNull();
        assertFalse(provider.isObject(nil));
        assertFalse(provider.isArray(nil));
        assertFalse(provider.isLiteral(nil));
        assertTrue(provider.isNull(nil));
    }
}
