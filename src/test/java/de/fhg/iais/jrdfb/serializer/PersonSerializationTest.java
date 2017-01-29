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
        Student student = new Student();
        student.setName("Ali Arslan");
        student.setMatrNo(111111);

        Address address = new Address("Bonn", "Germany");
        address.setStreet("Romerstraße");

        student.setAddress(address);
        assertEquals(serializer.serialize(student).trim(), rdf_turtle.trim());
    }
}
