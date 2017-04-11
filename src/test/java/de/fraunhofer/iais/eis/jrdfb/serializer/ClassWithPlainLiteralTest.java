package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.datatype.PlainLiteral;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithLangLiterals;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithPlainLiteral;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ClassWithPlainLiteralTest {
    RdfSerializer serializer;
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        serializer = new RdfSerializer(ClassWithPlainLiteral.class);

        rdf_turtle = FileUtils.readResource("ClassWithPlainLiteral.ttl", this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
    }

    @Test
    public void testSerialize() throws Exception {
        ClassWithPlainLiteral classWithPlainLiteral = new ClassWithPlainLiteral();
        classWithPlainLiteral.setSinglePlainLiteral(new PlainLiteral("test_literal_single", "en"));


        String serializedTurtle = serializer.serialize(classWithPlainLiteral).trim();

        Model actualModel = ModelFactory.createDefaultModel();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        assertTrue(expectedModel.isIsomorphicWith(actualModel));

    }

    @Test
    public void testDeserialize() throws Exception{
        ClassWithPlainLiteral classWithPlainLiteral = (ClassWithPlainLiteral)serializer.deserialize(rdf_turtle);

        assertEquals(classWithPlainLiteral.getSinglePlainLiteral().getLang(), "en");
        assertEquals(classWithPlainLiteral.getSinglePlainLiteral().getValue(), "test_literal_single");

        /*
        Literal first = ((ArrayList<Literal>)classWithLangLiterals.getLiteralCollection()).get(0);
        Literal second = ((ArrayList<Literal>)classWithLangLiterals.getLiteralCollection()).get(1);
        assertEquals(first.getLanguage(), "en");
        assertEquals(first.getString(), "test_literal_1");

        assertEquals(second.getLanguage(), "de");
        assertEquals(second.getString(), "test_literal_2");
        */
    }

}