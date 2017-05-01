package de.fraunhofer.iais.eis.jrdfb.util;

import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

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

    public static Model getSerializedModel(RdfMarshaller serializer, Object object)
            throws Exception{
        Model actualModel = ModelFactory.createDefaultModel();
        String serializedTurtle = serializer.marshal(object).trim();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");

        return actualModel;
    }
}
