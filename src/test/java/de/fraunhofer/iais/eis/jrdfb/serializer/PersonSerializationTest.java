package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Address;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Person;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Student;
import de.fraunhofer.iais.eis.jrdfb.serializer.util.SerializerSingleton;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class PersonSerializationTest {
    RdfSerializer serializer = SerializerSingleton.getInstance();
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        rdf_turtle = FileUtils
                .readResource("Person.ttl",
                        this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
    }

    @Test
    public void testSerializeNestedProperties() throws Exception{
        Student student = new Student("Ali Arslan", 111111);

        Address address = new Address("Bonn", "Germany");
        address.setStreet("Romerstraße");
        address.setLongitude(7.1847);
        address.setLatitude(50.7323);
        address.setMapUrl(new URL("http://example.com/address/1"));
        student.setAddress(address);
        student.setProfileUrl(new URL("http://example.com/profile/1"));

        GregorianCalendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        c.set(1989, 8, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        XMLGregorianCalendar birthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        student.setBirthDate(birthDate);

        Model actualModel = ModelFactory.createDefaultModel();
        String serializedTurtle = serializer.serialize(student).trim();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testDeserializeNestedProperties() throws Exception{
        Student student = (Student)serializer.deserialize(rdf_turtle);

        assertEquals(student.getName(), "Ali Arslan");
        assertEquals(student.getMatrNo(), 111111);

        assertEquals(student.getAddress().getCity(), "Bonn");
        assertEquals(student.getAddress().getCountry(), "Germany");
        assertEquals(student.getAddress().getStreet(), "Romerstraße");
        assertEquals(student.getAddress().getLongitude(), 7.1847);
        assertEquals(student.getAddress().getLatitude(), 50.7323);
        assertEquals(student.getBirthDate().getDay(), 1);
        assertEquals(student.getBirthDate().getMonth(), 9);
        assertEquals(student.getBirthDate().getYear(), 1989);
        assertEquals(student.getProfileUrl().toExternalForm(),
                "http://example.com/profile/1");
    }


    /**
     * Test serialization of Null valued annotated property
     * Related to: EISMASTER-4
     *
     */
    @Test
    public void testWithNullValuedProperties(){
        Student student = new Student(null, 111111);
        boolean exceptionThrown = false;
        String rdf = null;
        try {
            rdf = serializer.serialize(student);
            System.out.println(rdf);
            Student deserialized = (Student)serializer.deserialize(rdf);
            assertEquals(deserialized.getMatrNo(), 111111);
        } catch (Exception e) {
            exceptionThrown = true;
            e.printStackTrace();
        }
        assertFalse(exceptionThrown);
    }
}
