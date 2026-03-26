package org.aksw.graphql4sparql.cmd;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Stream;

import org.aksw.jenax.graphql.schema.generator.GraphQlSchemaGenerator;
import org.aksw.jenax.graphql.schema.generator.GraphQlSchemaGenerator.TypeInfo;
import org.aksw.jenax.graphql.schema.generator.GraphQlSchemaSummarizer;
import org.aksw.jenax.graphql.sparql.v2.schema.GraphQlSchemaUtils;
import org.aksw.jenax.graphql.util.GraphQlUtils;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.system.G;
import org.apache.jena.vocabulary.RDFS;

import graphql.language.AstPrinter;
import graphql.language.Definition;
import graphql.language.Document;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "schemagen", description = "Generate a schema GraphQL Schema over RDF data in files or in a SPARQL endpoinst.")
public class CmdGraphQl4SparqlSchemaGen
    implements Callable<Integer> {

    @Option(names = { "-h", "--help" }, usageHelp = true)
    public boolean help = false;

    @Option(names = "--meta", negatable = true,
        defaultValue = "true", fallbackValue = "true",
        description = "Include meta schema definitions. True by default.")
    public boolean includeMetaSchemaDefinitions;

    @Option(names = { "-l", "--label-source" }, description = "An RDF document with labels for the classes and properties. Local names will be used as fallback.")
    public String labelSource;

    @Parameters(arity = "0..*", description = "Input files")
    public List<String> nonOptionArgs = new ArrayList<>();

    @Override
    public Integer call() throws Exception {
        Graph labelGraph = labelSource == null
            ? null
            : RDFDataMgr.loadGraph(labelSource);

        Graph dataGraph = GraphFactory.createDefaultGraph();
        for (String inputFileArg : nonOptionArgs) {
            DatasetGraph contribDsg = RDFDataMgr.loadDatasetGraph(inputFileArg);
            // Merge all default and named graphs into a single graph.
            // XXX Perhaps make configurable which graphs to pass on to the schema generator.
            G.addInto(dataGraph, contribDsg.getDefaultGraph());
            G.addInto(dataGraph, contribDsg.getUnionGraph());
        }

        List<TypeInfo> types = GraphQlSchemaSummarizer.summarize(dataGraph);

        Function<String, String> iriToLabel = labelGraph == null
            ? null
            : iriStr -> {
                try (Stream<String> stream = labelGraph.stream(
                    NodeFactory.createURI(iriStr), RDFS.label.asNode(), Node.ANY)
                .map(Triple::getObject)
                .filter(Node::isLiteral)
                .map(Node::getLiteralLexicalForm)) {
                    return stream.findFirst().orElse(null);
                }
            };

        GraphQlSchemaGenerator generator = new GraphQlSchemaGenerator(iriToLabel);
        Document schemaDoc = generator.process(types);

        if (includeMetaSchemaDefinitions) {
            Document metaDoc = GraphQlSchemaUtils.loadMetaSchema();

            List<Definition> mergedDefinitions = new ArrayList<>();
            mergedDefinitions.addAll(metaDoc.getDefinitions());
            mergedDefinitions.addAll(schemaDoc.getDefinitions());

            // Create a new merged document
            Document mergedDoc = GraphQlSchemaUtils.merge(metaDoc, schemaDoc);
            schemaDoc = GraphQlSchemaUtils.harmonize(mergedDoc);
        }

        String str = AstPrinter.printAst(schemaDoc);

        try (Writer writer = new OutputStreamWriter(StdIoUtils.openStdOutWithCloseShield())) {
            writer.write(str);
        }

        boolean validateOutput = true;
        if (validateOutput) {
            @SuppressWarnings("unused")
            Document reparsedDoc = GraphQlUtils.parseUnrestricted(str);
        }

        return 0;
    }
}
