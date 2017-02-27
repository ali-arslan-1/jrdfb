package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithLangLiterals;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ClassWithLangLiteralsTest {
    RdfSerializer serializer;
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        serializer = new RdfSerializer(ClassWithLangLiterals.class);
        rdf_turtle = FileUtils
                .readResource("PersonWithCollection.ttl",
                        this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
    }

    @Test
    public void testSerialize() throws Exception{
        ClassWithLangLiterals classWithLangLiterals = new ClassWithLangLiterals();
        classWithLangLiterals.setSingleLiteral(expectedModel
                                                .createLiteral("test_literal_single","en"));

        Collection<Literal> literalCollection = new ArrayList<>();
        literalCollection.add(expectedModel
                                .createLiteral("test_literal_1","en"));

        literalCollection.add(expectedModel
                .createLiteral("test_literal_2","de"));

        classWithLangLiterals.setLiteralCollection(literalCollection);

        String serializedTurtle = serializer.serialize(classWithLangLiterals).trim();

        Model actualModel = ModelFactory.createDefaultModel();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        assertTrue(expectedModel.isIsomorphicWith(actualModel));


    }

}