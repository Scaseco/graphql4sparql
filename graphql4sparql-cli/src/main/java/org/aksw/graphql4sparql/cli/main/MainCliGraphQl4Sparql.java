package org.aksw.graphql4sparql.cli.main;

import picocli.CommandLine;

public class MainCliGraphQl4Sparql {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new CmdGraphQl4SparqlMain());
        commandLine.execute(args);
    }
}
