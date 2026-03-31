package org.aksw.graphql4sparql.cli.main;

import org.aksw.graphql4sparql.cli.util.VersionProviderGraphql4SparqlCli;
import org.aksw.graphql4sparql.cmd.CmdGraphQl4SparqlSchemaGen;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name="graphql4sparql",
        description = "GraphQl4Sparql Command Line Interface",
        versionProvider = VersionProviderGraphql4SparqlCli.class,
        subcommands = {
                CmdGraphQl4SparqlSchemaGen.class
        }
)
public class CmdGraphQl4SparqlMain {
    @Option(names = { "-h", "--help" }, usageHelp = true)
    public boolean help = false;

    @Option(names = { "-v", "--version" }, versionHelp = true)
    public boolean version = false;
}
