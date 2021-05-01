package de.chst;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutlineParser {
    static final String REGEX_OUTLINE_ENTRY = "^(?<chapter>[0-9\\.]+ )?(?<heading>.+?)\\.{2,}(?<pagenum>[0-9]+)$";
    static final Pattern OUTLINE_PATTERN = Pattern.compile(REGEX_OUTLINE_ENTRY);

    private OutlineParser() {

    }

    /**
     *
     * @param lines the outline in the specified text format
     */
    public static Graph<OutlineEntry> parseLines(Iterable<String> lines) {
        Graph<OutlineEntry> g = new Graph<>(new Node<>(null));

        for (String line : lines) {
            Matcher m = OUTLINE_PATTERN.matcher(line);

            /* Ignore invalid entries */
            if (!m.find()) continue;

            String chapterNum = m.group("chapter");

            addToGraph(g,
                    (chapterNum == null) ? "" : chapterNum,
                    m.group("heading"),
                    Integer.parseInt(m.group("pagenum")));
        }

        return g;
    }

    private static void addToGraph(Graph<OutlineEntry> g, String chapterNum, String description, int page) {
        /* Create stack with chapter number parts, most significant digit on top */
        var chapterNums = new Stack<String>();
        String[] parts = chapterNum.split("\\.");

        if (!chapterNum.equals("")) {
            for (int i = parts.length - 2; i >= 0; i--) {
                chapterNums.push(parts[i]);
            }
        }

        var lastChapterPart = parts[parts.length - 1].trim();
        recursiveAdd(g.getRoot(), chapterNums, new OutlineEntry(lastChapterPart, description, page));
    }

    private static void recursiveAdd(Node<OutlineEntry> n, Stack<String> chapterNums, OutlineEntry e) {
        if (chapterNums.empty()) {
            /* We have reached our destination */
            n.addChild(new Node<OutlineEntry>(e));
            return;
        }

        /* Find correct child node. */
        String currentSubPart = chapterNums.pop();
        for (Node<OutlineEntry> child : n.getChildren()) {
            boolean bisChild = isChild(child, e, currentSubPart);
            if (bisChild) {
                /* Descend into current subgraph */
                recursiveAdd(child, chapterNums, e);
                return;
            }
        }

        /* Could not find correct child node, create inner node. */
        Node<OutlineEntry> newChild = new Node<OutlineEntry>(
                new OutlineEntry(currentSubPart, "", -1));
        n.addChild(newChild);
        recursiveAdd(newChild, chapterNums, e);
    }

    private static boolean isChild(Node<OutlineEntry> parent, OutlineEntry e, String currentChapterPart) {
        if (parent.getValue().getChapterNum().equals(""))
            return false;

        String parentLabel = parent.getValue().getChapterNum();

        return currentChapterPart.equals(parentLabel);
    }

    private static boolean isInnerNode(Node<OutlineEntry> n) {
        return n.getValue() != null && n.getValue().getPageNumber() == -1;
    }

}
