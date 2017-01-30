package de.fhg.iais.jrdfb.serializer;

import de.fhg.iais.jrdfb.serializer.example.Address;
import de.fhg.iais.jrdfb.serializer.example.Person;
import de.fhg.iais.jrdfb.serializer.example.Student;
import de.fhg.iais.jrdfb.util.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class PersonSerializationTest {
    RdfSerializer serializer;
    String rdf_turtle;

    @BeforeClass
    public void setUp() throws Exception {
        serializer = new RdfSerializer(Person.class, Student.class, Address.class);
        rdf_turtle = FileUtils
                .readResource("Person.ttl",
                        this.getClass());

    }

    @Test
    public void testSerializeNestedProperties() throws Exception{
        Student student = new Student("Ali Arslan", 111111);

        Address address = new Address("Bonn", "Germany");
        address.setStreet("Romerstraße");

        student.setAddress(address);
        assertEquals(serializer.serialize(student).trim(), rdf_turtle.trim());
    }

    @Test
    public void testDeserializeNestedProperties() throws Exception{
        Student student = (Student)serializer.deserialize(rdf_turtle);

        assertEquals(student.getName(), "Ali Arslan");
        assertEquals(student.getMatrNo(), 111111);

        assertEquals(student.getAddress().getCity(), "Bonn");
        assertEquals(student.getAddress().getCountry(), "Germany");
        assertEquals(student.getAddress().getStreet(), "Romerstraße");

    }
}
