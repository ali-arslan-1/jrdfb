package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Bar;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Foo;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ImplMultipleInterfaces;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.RdfUnmarshaller;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import de.fraunhofer.iais.eis.jrdfb.util.TestUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ImplMultipleInterfacesTest {
    RdfMarshaller marshaller;
    RdfUnmarshaller unmarshaller;
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
    public void testSerializeWithOrder1() throws Exception {
        marshaller = new RdfMarshaller(ImplMultipleInterfaces.class, Foo.class, Bar.class);
        ImplMultipleInterfaces obj = new ImplMultipleInterfaces("foo", "bar");
        Model actualModel = TestUtils.getSerializedModel(marshaller, obj);
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testSerializeWithOrder2() throws Exception {
        marshaller = new RdfMarshaller(Foo.class, Bar.class, ImplMultipleInterfaces.class);
        ImplMultipleInterfaces obj = new ImplMultipleInterfaces("foo", "bar");
        Model actualModel = TestUtils.getSerializedModel(marshaller, obj);
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testSerializeWithOrder3() throws Exception {
        marshaller = new RdfMarshaller(Bar.class, Foo.class, ImplMultipleInterfaces.class);
        ImplMultipleInterfaces obj = new ImplMultipleInterfaces("foo", "bar");
        Model actualModel = TestUtils.getSerializedModel(marshaller, obj);
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testSerializeWithOrder4() throws Exception {
        marshaller = new RdfMarshaller(Bar.class, ImplMultipleInterfaces.class, Foo.class);
        ImplMultipleInterfaces obj = new ImplMultipleInterfaces("foo", "bar");
        Model actualModel = TestUtils.getSerializedModel(marshaller, obj);
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testDeserializeWithOrder1() throws Exception {
        unmarshaller = new RdfUnmarshaller(ImplMultipleInterfaces.class, Foo.class, Bar.class);
        ImplMultipleInterfaces obj = (ImplMultipleInterfaces) unmarshaller.unmarshal(rdf_turtle);
        assertEquals(obj.getBar(), "bar");
        assertEquals(obj.getFoo(), "foo");
    }

    @Test
    public void testDeserializeWithOrder2() throws Exception {
        unmarshaller = new RdfUnmarshaller(Foo.class, Bar.class, ImplMultipleInterfaces.class);
        ImplMultipleInterfaces obj = (ImplMultipleInterfaces) unmarshaller.unmarshal(rdf_turtle);
        assertEquals(obj.getBar(), "bar");
        assertEquals(obj.getFoo(), "foo");
    }

    @Test
    public void testDeserializeWithOrder3() throws Exception {
        unmarshaller = new RdfUnmarshaller(Bar.class, Foo.class, ImplMultipleInterfaces.class);
        ImplMultipleInterfaces obj = (ImplMultipleInterfaces) unmarshaller.unmarshal(rdf_turtle);
        assertEquals(obj.getBar(), "bar");
        assertEquals(obj.getFoo(), "foo");
    }
}