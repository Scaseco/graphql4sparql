package org.aksw.graphql4sparql.engine.api2;

import org.aksw.graphql4sparql.engine.model.ElementNode;
import org.apache.jena.sparql.core.Var;

/** Reference to a variable in a field. */
public record FieldVar(ElementNode field, Var var) {}
