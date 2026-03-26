package org.aksw.graphql4sparql.engine.schema;

import java.util.List;

import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.Element;

public record Fragment(Element element, List<Var> vars) {
}
