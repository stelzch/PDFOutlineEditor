import de.chst.Graph;
import de.chst.Node;
import de.chst.OutlineEntry;
import de.chst.OutlineParser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OutlineParserTests {

    @Test
    public void testFlatHierarchy() {
        String[] lines = {
          "Table of Contents......5",
          "Chapter 1..............6",
          "Chapter 2.............11",
          "Final remarks.........99"
        };

        List<OutlineEntry> expected = Arrays.asList(
                new OutlineEntry("", "Table of Contents", 5),
                new OutlineEntry("", "Chapter 1", 6),
                new OutlineEntry("", "Chapter 2", 11),
                new OutlineEntry("", "Final remarks", 99));

        Graph<OutlineEntry> g = OutlineParser.parseLines(Arrays.asList(lines));
        List<Node<OutlineEntry>> entries = g.getRoot().getChildren();

        assertEquals(expected.size(), entries.size());

        for(int i = 0; i <expected.size(); i++) {
            assertEquals(expected.get(i), entries.get(i).getValue());
            assertFalse(entries.get(i).hasChildren());
        }
    }
}
