package org.aksw.graphql4sparql.schemagen.cli.main;

import org.aksw.graphql4sparql.cmd.CmdGraphQlSchemaGen;

import picocli.CommandLine;

public class MainCliGraphQlSchemaGen {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new CmdGraphQlSchemaGen());
        commandLine.execute(args);
    }
}
