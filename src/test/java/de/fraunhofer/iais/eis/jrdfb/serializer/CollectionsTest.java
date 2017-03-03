package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Person;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Student;
import de.fraunhofer.iais.eis.jrdfb.serializer.util.SerializerSingleton;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionsTest {
    RdfSerializer serializer = SerializerSingleton.getInstance();
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        rdf_turtle = FileUtils
                .readResource("PersonWithCollection.ttl",
                        this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
    }

    @Test
    public void testSerializeNestedCollection() throws Exception{
        Student student = new Student("Ali Arslan", 111111);
        student.setProfileUrl(new URL("http://example.com/profile/1"));

        Student friend1 = new Student("Nabeel Muneer", "222222");
        Student friend2 = new Student("Abdullah Hamid", "333333");

        List<Person> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(friend2);

        student.setFriends(friends);

        Model actualModel = ModelFactory.createDefaultModel();
        String serializedTurtle = serializer.serialize(student).trim();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testDeserializeNestedCollection() throws Exception{
        Student student = (Student)serializer.deserialize(rdf_turtle);
        List<Person> friends = student.getFriends();

        assertEquals(student.getName(), "Ali Arslan");
        assertEquals(student.getMatrNo(), 111111);
        assertEquals(student.getProfileUrl().toExternalForm(),
                "http://example.com/profile/1");

        Person friend1 = friends.get(0);
        Person friend2 = friends.get(1);

        assertEquals(friend1.getName(), "Nabeel Muneer");
        assertEquals(friend1.getSsn(), "222222");

        assertEquals(friend2.getName(), "Abdullah Hamid");
        assertEquals(friend2.getSsn(), "333333");

    }

    @Test
    public void testCollectionWithNullValues() throws Exception{
        Student student = new Student("Ali Arslan", 111111);
        student.setProfileUrl(new URL("http://example.com/profile/1"));

        Student friend1 = new Student("Nabeel Muneer", "222222");

        List<Person> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(null);

        student.setFriends(friends);

        boolean exceptionThrown = false;
        String rdf;
        try {
            rdf = serializer.serialize(student);
            System.out.println(rdf);
            Student deserialized = (Student)serializer.deserialize(rdf);
            assertEquals(deserialized.getMatrNo(), 111111);
            Person p1 = student.getFriends().get(0);
            Person p2 = student.getFriends().get(1);

            assertEquals(p1.getName(), "Nabeel Muneer");
            assertEquals(p1.getSsn(), "222222");

            assertNull(p2);
        } catch (Exception e) {
            exceptionThrown = true;
            e.printStackTrace();
        }

        assertFalse(exceptionThrown);
    }

}
