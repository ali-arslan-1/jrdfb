package de.fraunhofer.iais.eis.jrdfb.util;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class TestUtils {

    @Test
    public void testPluckIdFromUriWhenInBetween() throws Exception {
        assertEquals("1234",
                Utils.pluckIdFromUri("example/1234/something",
                                "example/{RdfId}/something"));
    }

    @Test
    public void testPluckIdFromUriWhenInEnd() throws Exception {
        assertEquals("1234",
                Utils.pluckIdFromUri("example/1234",
                        "example/{RdfId}"));
    }

    @Test
    public void testPluckIdFromUriWhenTemplateEmpty() throws Exception {
        assertEquals("example/1234",
                Utils.pluckIdFromUri("example/1234",
                        ""));
    }
}
