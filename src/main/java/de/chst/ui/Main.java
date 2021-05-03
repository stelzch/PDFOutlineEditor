package de.chst.ui;

import de.chst.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.kohsuke.args4j.CmdLineException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CommandLineArgs a = null;
        try {
            a = new CommandLineArgs(args);
        } catch (CmdLineException e) {
            return;
        }

        PDDocument doc;
        try {
            doc = Loader.loadPDF(a.inputFile);
        } catch (IOException e) {
            System.err.println("Unable to parse input file: " + e.getLocalizedMessage());
            return;
        }


        ArrayList<String> lines = new ArrayList<>();
        try {
            FileInputStream inp = new FileInputStream(a.outlineFile);
            Scanner sc = new Scanner(inp);

            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        } catch (IOException e) {
            System.err.println("Could read outline file: " + e.getLocalizedMessage());
            return;
        }

        PageResolver resolver = null;
        if (a.resolvePageNames) {
            resolver = new PageResolver(doc);
        }
        Graph<OutlineEntry> outline = OutlineParser.parseLines(lines, a.resolvePageNames, resolver);
        OutlineInjector.inject(doc, outline);

        long fileSize = a.inputFile.length();


        try {
            if (a.outputFile != a.inputFile) {
                doc.save(a.outputFile);
            } else {
                /* Buffer file in memory */
                ByteArrayOutputStream buffer;
                if (fileSize < Integer.MAX_VALUE) {
                    buffer = new ByteArrayOutputStream((int) fileSize);
                } else {
                    buffer = new ByteArrayOutputStream((int) a.inputFile.length());
                }

                doc.save(buffer);
                buffer.writeTo(new FileOutputStream(a.outputFile));
            }
        } catch(IOException e) {
            System.err.println("Could not write to output file:" + e.getLocalizedMessage());
        }

    }
}
