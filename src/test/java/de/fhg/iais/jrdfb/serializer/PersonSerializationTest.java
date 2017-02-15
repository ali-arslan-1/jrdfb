package de.fhg.iais.jrdfb.serializer;

import de.fhg.iais.jrdfb.RdfSerializer;
import de.fhg.iais.jrdfb.serializer.example.Address;
import de.fhg.iais.jrdfb.serializer.example.Person;
import de.fhg.iais.jrdfb.serializer.example.Student;
import de.fhg.iais.jrdfb.util.FileUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
        assertEquals(student.getAddress().getLongitude(), 7.1847);
        assertEquals(student.getAddress().getLatitude(), 50.7323);
        assertEquals(student.getBirthDate().getDay(), 1);
        assertEquals(student.getBirthDate().getMonth(), 9);
        assertEquals(student.getBirthDate().getYear(), 1989);
        assertEquals(student.getProfileUrl().toExternalForm(),
                "http://example.com/profile/1");
    }
}
