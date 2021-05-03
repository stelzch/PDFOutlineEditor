package de.chst;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDPageLabels;

import java.io.IOException;
import java.util.Map;

public class PageResolver {

    Map<String, Integer> lookupMap;

    public PageResolver(PDDocument doc) {
        try {
            lookupMap = doc.getDocumentCatalog().getPageLabels().getPageIndicesByLabels();
        } catch(IOException e) {
            lookupMap = null;
        }
    }

    private void precachePageLabels(PDPageLabels labels) {
        labels.getLabelsByPageIndices();
    }

    /**
     * Get the page number that corresponds to the specified label
     *
     * @argument label The page label
     * @returns -1 if the page label could not be resolved to a page, or the real page number (zero-indexed) otherwise
     */
    public int getRealPageNumber(String label) {
        if (lookupMap == null || !lookupMap.containsKey(label))
            return -1;

        return lookupMap.get(label);
    }
}
