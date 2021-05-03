package de.chst;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

public class OutlineInjector {
    public static void inject(PDDocument doc, Graph<OutlineEntry> graph) {
        doc.getDocumentCatalog().setDocumentOutline(outlineFromGraph(graph));
    }

    private static PDDocumentOutline outlineFromGraph(Graph<OutlineEntry> g) {
        PDDocumentOutline outline = new PDDocumentOutline();
        outlineFromGraphRecursive(g.getRoot(), outline, "");

        return outline;
    }

    private static void outlineFromGraphRecursive(Node<OutlineEntry> n, PDOutlineNode parent, String prefix) {
        for (var c : n.getChildren()) {
            OutlineEntry info = c.getValue();

            String label = prefix + info.getChapterNum() + " " + info.getDescription();

            COSDictionary props = new COSDictionary();
            props.setString("Title", label);
            props.setItem("Parent", parent);

            PDOutlineItem childItem = new PDOutlineItem();
            childItem.setTitle(label);

            if (info.getPageNumber() != -1) {
                PDPageFitDestination dst = new PDPageFitDestination();
                dst.setPageNumber(info.getPageNumber());
                childItem.setDestination(dst);
            }

            parent.addLast(childItem);
            outlineFromGraphRecursive(c, childItem, prefix + info.getChapterNum() + ".");
        }
    }
}
