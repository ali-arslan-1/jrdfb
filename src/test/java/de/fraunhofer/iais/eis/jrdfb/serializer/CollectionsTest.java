package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Person;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Student;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionsTest {
    RdfSerializer serializer;
    String rdf_turtle;
    Model expectedModel;

    @BeforeClass
    public void setUp() throws Exception {
        serializer = new RdfSerializer(Person.class, Student.class);
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

        Student friend1 = (Student)friends.get(0);
        Student friend2 = (Student)friends.get(1);

        assertEquals(friend1.getName(), "Nabeel Muneer");
        assertEquals(friend1.getMatrNo(), 222222);

        assertEquals(friend2.getName(), "Abdullah Hamid");
        assertEquals(friend2.getMatrNo(), 333333);

    }

}
