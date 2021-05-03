package de.chst.ui;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

import java.io.File;

public class CommandLineArgs {
    @Option(name = "-r", aliases = { "--resolve-page-labels"}, usage = "Consider the page numbers as page labels.")
    boolean resolvePageNames;

    @Argument(required = true, usage = "The input PDF file", index = 0)
    File inputFile;

    @Argument(required = true, usage = "A file specifying the outline", index = 1)
    File outlineFile;

    @Argument(required = true, usage = "Output file", index = 2)
    File outputFile;

    public CommandLineArgs(String... args) {
        CmdLineParser p = new CmdLineParser(this);
        try {
            p.parseArgument(args);

            if (!inputFile.exists()) {
                throw new CmdLineException("Input file does not exist");
            }

            if (!outlineFile.exists()) {
                throw new CmdLineException("Outline file does not exist");
            }

            if (!outputFile.canWrite()) {
                //throw new CmdLineException("Unable to write to output file");
            }
        } catch(CmdLineException e) {
            System.err.println(e.getLocalizedMessage());
            p.printUsage(System.err);
        }
    }
}
