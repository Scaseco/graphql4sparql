package org.aksw.graphql4sparql.engine.api2;

import java.util.Set;
import java.util.function.Function;

import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.Element;

/**
 * A transformation that can be applied to an element.
 */
public interface ElementTransform
    extends Function<Element, Element>
{
    /**
     * Returns the variables declared by this transformation.
     *
     * @return The set of declared variables
     */
    Set<Var> getDeclaredVariables();
}
