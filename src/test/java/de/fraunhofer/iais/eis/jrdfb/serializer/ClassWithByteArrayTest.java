package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithByteArray;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.RdfUnmarshaller;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ClassWithByteArrayTest {
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        rdf_turtle = FileUtils
                .readResource("ClassWithByteArray.ttl",
                        this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()),
                                                        null, "TURTLE");
    }

    @Test
    public void testSerialize() throws Exception{
        RdfMarshaller marshaller = new RdfMarshaller(ClassWithByteArray.class);

        ClassWithByteArray classWithByteArray = new ClassWithByteArray();
        classWithByteArray.setByteArrayLiteral("xxx".getBytes(Charset.defaultCharset()));

        String serializedTurtle = marshaller.marshal(classWithByteArray).trim();

        Model actualModel = ModelFactory.createDefaultModel();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);

        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testDeserialize() throws Exception{
        RdfUnmarshaller unmarshaller = new RdfUnmarshaller(ClassWithByteArray.class);

        ClassWithByteArray classWithByteArray =
                (ClassWithByteArray)unmarshaller.unmarshal(rdf_turtle);

        assertEquals(new String(classWithByteArray.getByteArrayLiteral()),
                "xxx");

    }
}