package de.fhg.iais.jrdfb.serializer;
import de.fhg.iais.jrdfb.RdfSerializer;
import de.fhg.iais.jrdfb.serializer.example.Day;
import de.fhg.iais.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class EnumTest {
    RdfSerializer serializer;
    String rdf_turtle;
    Resource expectedRes;
    Model expectedModel;


    @BeforeClass
    public void setUp() throws Exception {
        serializer = new RdfSerializer(Day.class);
        rdf_turtle = FileUtils
                .readResource("Day.ttl",
                        this.getClass());

        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
        ResIterator iter = expectedModel.listResourcesWithProperty(RDF.type,
                expectedModel.createProperty("http://www.w3.org/2006/time#day"));
        expectedRes = iter.nextResource();
    }
    @Test
    public void testSerializeSingleEnum() throws Exception{
        Day day = Day.FRIDAY;

        Model actualModel = ModelFactory.createDefaultModel();
        String serializedTurtle = serializer.serialize(day).trim();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                    null, "TURTLE");
        ResIterator iter = actualModel.listResourcesWithProperty(RDF.type,
                actualModel.createProperty("http://www.w3.org/2006/time#day"));
        Resource actualRes = iter.nextResource();

        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testDeserializeSingleEnum() throws Exception{
        Day day = (Day)serializer.deserialize(rdf_turtle);
        assertEquals(day, Day.FRIDAY);
    }
}
