package de.fraunhofer.iais.eis.jrdfb.util;

import de.fraunhofer.iais.eis.jrdfb.serializer.ClassWithLangLiteralsTest;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithLangLiterals;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Person;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Student;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ids.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ReflectUtilsTest {
    @Test
    public void testGetResourceClass() throws Exception {
        String rdf_turtle = FileUtils
                .readResource("ClassWithLangLiterals.ttl",
                        ClassWithLangLiteralsTest.class);
        Model expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");

        ResIterator resIterator = expectedModel.listResourcesWithProperty(RDF.type, expectedModel
                .createProperty("example:ClassWithLangLiterals"));
        Resource res  = resIterator.next();

        assertEquals(ReflectUtils.getResourceClass(res), ClassWithLangLiterals.class);

    }

    @Test
    public void getIfAssignableFromAny() throws Exception {

        assertEquals(ReflectUtils.getIfAssignableFromAny(
                new Class<?>[]{Person.class, Student.class}, Person.class.getName()), Person.class);

        assertEquals(ReflectUtils.getIfAssignableFromAny(
                new Class<?>[]{Person.class, Student.class}, Student.class.getName()), Student
                .class);

        assertEquals(ReflectUtils.getIfAssignableFromAny(
                new Class<?>[]{
                        Dataset.class,
                        Instant.class,
                        Interval.class,
                        TemporalEntity.class
                        }, IntervalImpl.class.getName()),

                Interval.class);


        assertEquals(ReflectUtils.getIfAssignableFromAny(
                new Class<?>[]{
                        Dataset.class,
                        Instant.class,
                        TemporalEntity.class
                }, IntervalImpl.class.getName()),

                TemporalEntity.class);

    }
}