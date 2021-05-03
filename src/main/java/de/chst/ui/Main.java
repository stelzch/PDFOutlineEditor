package de.chst.ui;

import de.chst.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CommandLineArgs a = new CommandLineArgs(args);

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

        try {
            doc.save(a.outputFile);
        } catch(IOException e) {
            System.err.println("Could not write to output file:" + e.getLocalizedMessage());
        }

    }
}
