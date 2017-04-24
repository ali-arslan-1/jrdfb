package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Bar;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Foo;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ImplMultipleInterfaces;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ImplMultipleInterfacesTest {
    RdfSerializer serializer;
    String rdf_turtle;
    Model expectedModel;


    @BeforeMethod
    public void setUp() throws Exception {
        rdf_turtle = FileUtils
                .readResource("ImplMultipleInterfaces.ttl",
                        this.getClass());

        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
    }

    @Test
    public void testSerialize() throws Exception {
        serializer = new RdfSerializer(ImplMultipleInterfaces.class, Foo.class, Bar.class);
        ImplMultipleInterfaces obj = new ImplMultipleInterfaces();
        Model actualModel = ModelFactory.createDefaultModel();
        String serializedTurtle = serializer.serialize(obj).trim();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }
}