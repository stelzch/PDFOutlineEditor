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
        Graph<OutlineEntry> g = new Graph<>(new Node<OutlineEntry>(null));

        for (String line : lines) {
            Matcher m = OUTLINE_PATTERN.matcher(line);

            /* Ignore invalid entries */
            if (!m.find()) continue;

            String chapterNum = m.group("chapter");

            OutlineEntry e = new OutlineEntry(
                    (chapterNum == null) ? "" : chapterNum,
                    m.group("heading"),
                    Integer.parseInt(m.group("pagenum"))
            );

            addToGraph(g, e);
        }

        return g;
    }

    private static void addToGraph(Graph<OutlineEntry> g, OutlineEntry e) {
        /* Create stack with chapter number parts, most significant digit on top */
        Stack chapterNums = new Stack();
        if (e.getChapterNum() != "") {
            String[] parts = e.getChapterNum().split(".");
            for (int i = parts.length - 1; i >= 0; i--) {
                chapterNums.push(parts[i]);
            }
        }

        recursiveAdd(g.getRoot(), chapterNums, e);
    }

    private static void recursiveAdd(Node<OutlineEntry> n, Stack<String> chapterNums, OutlineEntry e) {
        if (chapterNums.empty()) {
            /* We have reached our destination */
            n.addChild(new Node(e));
            return;
        }

        /* Find correct child node. */
        String currentSubPart = chapterNums.pop();
        for (Node<OutlineEntry> child : n.getChildren()) {
            if (child.getValue().getChapterNum().equals(currentSubPart) &&
                    isInnerNode(child)) {
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

    private static boolean isInnerNode(Node<OutlineEntry> n) {
        return n.getValue() != null && n.getValue().getPageNumber() == -1;
    }

    public void getLevel() {

    }


}
