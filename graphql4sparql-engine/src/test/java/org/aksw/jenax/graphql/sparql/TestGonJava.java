package org.aksw.jenax.graphql.sparql;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.aksw.jenax.graphql.sparql.v2.gon.model.GonProviderJava;

public class TestGonJava
    extends TS_Gon<String, Object>
{
    public TestGonJava() {
        super(new GonProviderJava<>());
    }

    @Test
    public void test() {
        Object obj = provider.newObject();
        provider.setProperty(obj, "int", provider.newLiteral(1));

        Object arr = provider.newArray();
        provider.setProperty(obj, "array", arr);

        provider.addElement(arr, provider.newLiteral("foo"));
        provider.addElement(arr, provider.newLiteral(2));

        System.out.println(obj);
    }

    @Test
    public void testLiteral() {
        Object lit = provider.newLiteral("test");
        assertFalse(provider.isObject(lit));
        assertFalse(provider.isArray(lit));
        assertTrue(provider.isLiteral(lit));
        assertFalse(provider.isNull(lit));
    }
}

