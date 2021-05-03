import de.chst.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutlineInjectorTests {
    PDDocument doc;

    @BeforeEach
    public void setup() throws IOException {
        doc = Loader.loadPDF(Objects.requireNonNull(getClass().getResourceAsStream("ref.pdf")));
    }

    @Test
    public void testInjection() throws IOException {
        Graph<OutlineEntry> g = new Graph<>(new Node<OutlineEntry>(null, Collections.singletonList(
                new Node<>(new OutlineEntry("1", "Hello World", 6))
        )));

        OutlineInjector.inject(doc, g);

        assertEquals("1 Hello World",
                doc.getDocumentCatalog().getDocumentOutline().getFirstChild().getTitle());
    }
}
