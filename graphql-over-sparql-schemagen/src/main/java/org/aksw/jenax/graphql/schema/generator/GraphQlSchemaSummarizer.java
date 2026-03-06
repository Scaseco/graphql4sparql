package org.aksw.jenax.graphql.schema.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aksw.jenax.graphql.schema.generator.GraphQlSchemaGenerator.TypeInfo;
import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.system.G;
import org.apache.jena.vocabulary.XSD;

public class GraphQlSchemaSummarizer {
    public static final Node RDF_TYPE    = NodeFactory.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    public static final Node UNTYPED     = NodeFactory.createURI("urn:untyped");  // marker for untyped resources
    public static final Set<Node> UNTYPED_SET = Collections.singleton(UNTYPED);

    public static List<TypeInfo> summarize(Graph graph) {
        // -- Phase 1: collect all distinct subjects that have at least one triple --
        Map<Set<Node>, Map<Node, StatAccumulator>> sTypesToAccs = new HashMap<>();
        Iterator<Node> sIt = G.iterSubjects(graph);
        try { while (sIt.hasNext()) {
            Node s = sIt.next();
            Set<Node> sTypes = getTypesOf(graph, s);
            Map<Node, StatAccumulator> accs = sTypesToAccs.computeIfAbsent(sTypes, st -> new HashMap<>());

            Map<Node, Long> pToLiteralCount = new HashMap<>();
            Map<Node, Long> pToResourceCount = new HashMap<>();

            Iterator<Triple> tIt = graph.find(s, Node.ANY, Node.ANY);
            try { while (tIt.hasNext()) {
                Triple t = tIt.next();
                Node p = t.getPredicate();
                Node o = t.getObject();
                StatAccumulator acc = accs.computeIfAbsent(p, k -> new StatAccumulator(sTypes, p));
                if (o.isLiteral()) {
                    String dtStr = o.getLiteralDatatypeURI();
                    Node dt = dtStr != null
                        ? NodeFactory.createURI(dtStr)
                        : XSD.xstring.asNode(); // or use RDF plain literal marker

                    acc.literalDatatypes.add(dt);
                    pToLiteralCount.merge(p, 1l, Long::sum);
                } else {
                    Set<Node> oTypes = getTypesOf(graph, o);
                    acc.resourceTypes.addAll(oTypes);
                    pToResourceCount.merge(p, 1l, Long::sum);
                }
            } } finally { Iter.close(tIt); }

            for (var e : pToResourceCount.entrySet()) {
                Node pp = e.getKey();
                StatAccumulator pAcc = accs.get(pp);
                pAcc.resourceObjectsPerSubject = Math.max(pAcc.resourceObjectsPerSubject, e.getValue());
            }
            for (var e : pToLiteralCount.entrySet()) {
                Node pp = e.getKey();
                StatAccumulator pAcc = accs.get(pp);
                pAcc.literalCountPerSubject = Math.max(pAcc.literalCountPerSubject, e.getValue());
            }
        } } finally { Iter.close(sIt); }

        // Convert accumulators to result records
        List<TypeInfo> result = new ArrayList<>();
        for (var e1 : sTypesToAccs.entrySet()) {
            for (var e2 : e1.getValue().entrySet()) {
                Set<Node> sTypes = emptySetToUntyped(e1.getKey());
                Node p = e2.getKey();
                StatAccumulator acc = e2.getValue();

                boolean multiResource = acc.resourceObjectsPerSubject > 1;
                boolean multiLiteral = acc.literalCountPerSubject > 1;

                Set<Node> finalObjTypes = emptySetToUntyped(acc.resourceTypes);

                result.add(new TypeInfo(
                    sTypes,
                    p,
                    true, // forward direction only in this version
                    finalObjTypes,
                    multiResource,
                    acc.literalDatatypes,
                    multiLiteral
                ));
            }
        }
        // Optional: stable sort (e.g. by property URI lexical order)
        // result.sort(Comparator.comparing(t -> t.property().toString()));
        return result;
    }

    private static Set<Node> getTypesOf(Graph g, Node s) {
        if (!s.isURI() && !s.isBlank()) {
            return Collections.emptySet();
        }
        try (Stream<Node> stream = Iter.asStream(G.iterSP(g, s, RDF_TYPE)).filter(Node::isURI)) {
            Set<Node> types = stream.collect(Collectors.toCollection(HashSet::new));
            return types;
        }
    }

    private static Set<Node> emptySetToUntyped(Set<Node> set) {
        return set.isEmpty() ? UNTYPED_SET : set;
    }

    // Per-group accumulator
    private static class StatAccumulator {
        final Set<Node> sTypes;
        final Node property;
        final Set<Node> resourceTypes = new HashSet<>();
        final Set<Node> literalDatatypes = new HashSet<>();
        long literalCountPerSubject = 0;
        long resourceObjectsPerSubject = 0;

        // ctor args not really needed by extra information for debugging.
        StatAccumulator(Set<Node> sTypes, Node property) {
            this.sTypes = sTypes;
            this.property = property;
        }
    }
}
