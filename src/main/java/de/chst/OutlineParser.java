package de.chst;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * High level overview
 * ----------------------------
 *
 * This class is used to transform a set of strings like the following:
 *       Introduction..............................1
*        1.1 On the Importance of Keyboards........3
*        1.1.1 Mechanical Keyboards................4
*        1.1.2 Keyboard Cleaning...................6
*        2 Keeping Software updated...............11
*        2.1 Package Managers.....................12
*        4 Summary...............................323
*        Final remarks...........................999
 *  into a graph that represent their hierarchical structure. In this Graph, each Node will contain
 *  an OutlineEntry that has a chapter number (e.g. 1 for "Mechanical Keyboards"), a description, and a page
 *  number. The chapter number only contains the last part of its address "1.1.1", since the rest can be deduced from
 *  its parents nodes in the graph.
 *
 *  To use this, addToGraph constructs a stack that holds each component of the address ordered most-significant to
 *  least-significant. It then traverses the graph with the function recursiveAdd, finding the correct subgraph where
 *  the new node must be inserted.
 *
 *  If some entries are not specified (see testSkippedLevels-Testcase), inner nodes are created with an empty
 *  description and a page number of -1.
 */
public class OutlineParser {
    static final String REGEX_OUTLINE_ENTRY = "^(?<chapter>[0-9.]+ )?(?<heading>.+?)\\.{2,}(?<pagenum>[0-9]+)$";
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
            n.addChild(new Node<>(e));
            return;
        }

        /* Find correct child node. */
        String currentSubPart = chapterNums.pop();
        for (Node<OutlineEntry> child : n.getChildren()) {
            boolean bisChild = isChild(child, currentSubPart);
            if (bisChild) {
                /* Descend into current subgraph */
                recursiveAdd(child, chapterNums, e);
                return;
            }
        }

        /* Could not find correct child node, create inner node. */
        Node<OutlineEntry> newChild = new Node<>(
                new OutlineEntry(currentSubPart, "", -1));
        n.addChild(newChild);
        recursiveAdd(newChild, chapterNums, e);
    }

    private static boolean isChild(Node<OutlineEntry> parent, String currentChapterPart) {
        if (parent.getValue().getChapterNum().equals(""))
            return false;

        String parentLabel = parent.getValue().getChapterNum();

        return currentChapterPart.equals(parentLabel);
    }

}
