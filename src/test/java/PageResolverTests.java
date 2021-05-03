import de.chst.PageResolver;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageResolverTests {
    PDDocument doc;
    PageResolver pr;

    @BeforeEach
    public void setup() throws IOException {
        doc = Loader.loadPDF(getClass().getResourceAsStream("ref.pdf"));
        pr = new PageResolver(doc);
    }

    @ParameterizedTest
    @CsvSource({
            "i,        0",
            "xi,      10",
            "xviii,   17",
            "13,      32",
            "480,    499",
            "944,    963",
            "-1,      -1",
            "971,     -1"
    })
    public void testPageResolving(String label, int expectedNumber) {
        assertEquals(expectedNumber, pr.getRealPageNumber(label));
    }
}
