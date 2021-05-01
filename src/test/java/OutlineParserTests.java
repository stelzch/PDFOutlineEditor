import de.chst.Graph;
import de.chst.Node;
import de.chst.OutlineEntry;
import de.chst.OutlineParser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutlineParserTests {

    @Test
    public void testFlatHierarchy() {
        String[] lines = {
                "Table of Contents......5",
                "Chapter 1..............6",
                "Chapter 2.............11",
                "Final remarks.........99"
        };

        Graph<OutlineEntry> expected = new Graph<>(new Node<>(null, Arrays.asList(
                new Node<>(new OutlineEntry("", "Table of Contents", 5)),
                new Node<>(new OutlineEntry("", "Chapter 1", 6)),
                new Node<>(new OutlineEntry("", "Chapter 2", 11)),
                new Node<>(new OutlineEntry("", "Final remarks", 99)))));

        Graph<OutlineEntry> g = OutlineParser.parseLines(Arrays.asList(lines));
        assertEquals(expected, g);
    }

    @Test
    public void testHierachy() {
        String[] lines = {
                "Introduction..............................1",
                "1.1 On the Importance of Keyboards........3",
                "1.1.1 Mechanical Keyboards................4",
                "1.1.2 Keyboard Cleaning...................6",
                "2 Keeping Software updated...............11",
                "2.1 Package Managers.....................12",
                "4 Summary...............................323",
                "Final remarks...........................999",
        };

        Graph<OutlineEntry> expected = new Graph<>(new Node<>(null, Arrays.asList(
                new Node<>(new OutlineEntry("", "Introduction", 1)),
                new Node<>(new OutlineEntry("1", "", -1), Arrays.asList(
                        new Node<>(new OutlineEntry("1", "On the Importance of Keyboards", 3), Arrays.asList(
                                new Node<>(new OutlineEntry("1", "Mechanical Keyboards", 4)),
                                new Node<>(new OutlineEntry("2", "Keyboard Cleaning", 6))
                        ))
                )),
                new Node<>(new OutlineEntry("2", "Keeping Software updated", 11), Arrays.asList(
                        new Node<>(new OutlineEntry("1", "Package Managers", 12))
                )),
                new Node<>(new OutlineEntry("4", "Summary", 323)),
                new Node<>(new OutlineEntry("", "Final remarks", 999))
        )));
        var actual = OutlineParser.parseLines(Arrays.asList(lines));
        assertEquals(expected, actual);
    }

    @Test
    public void testSkippedLevels() {
        String[] lines = {
                "1 High-Level Entry.............5",
                "1.2.3.2 Skipped a few........6"
        };

        var expected = new Graph<>(new Node<>(null,
                Arrays.asList(
                        new Node<>(new OutlineEntry("1", "High-Level Entry", 5), Arrays.asList(
                                new Node<>(new OutlineEntry("2", "", -1), Arrays.asList(
                                        new Node<>(new OutlineEntry("3", "", -1), Arrays.asList(
                                                new Node<>(new OutlineEntry("2", "Skipped a few", 6))
                                        )))
                                ))
                        ))));

        var actual = OutlineParser.parseLines(Arrays.asList(lines));
        assertEquals(expected, actual);
    }
}
