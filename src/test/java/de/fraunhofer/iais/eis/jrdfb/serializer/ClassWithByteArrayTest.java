package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithByteArray;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ClassWithLangLiterals;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.RdfUnmarshaller;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ClassWithByteArrayTest {
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        /*
        rdf_turtle = FileUtils
                .readResource("ClassWithLangLiterals.ttl",
                        this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
        */
    }

    @Test
    public void testSerialize() throws Exception{
        RdfMarshaller marshaller = new RdfMarshaller(ClassWithByteArray.class);

        ClassWithByteArray classWithByteArray = new ClassWithByteArray();
        classWithByteArray.setByteArrayLiteral("xxx".getBytes(Charset.defaultCharset()));
        //classWithByteArray.setByteArrayLiteral("xxx");

        String serializedTurtle = marshaller.marshal(classWithByteArray).trim();

        Model actualModel = ModelFactory.createDefaultModel();
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()), null, "TURTLE");

        Property cmapProp = actualModel.createProperty("http://jrdfb.iais.fraunhofer.de/vocab/classMapping");
        Property balProp = actualModel.createProperty("example:byteArrayLiteral");

        RDFNode cmap = actualModel.listObjectsOfProperty(cmapProp).next();
        RDFNode mappedClass = actualModel.listObjectsOfProperty(cmap.asResource(), balProp).next();

        Assert.assertFalse(mappedClass.toString().equals("[B"));
    }

    @Test
    public void testDeserialize() throws Exception{
        /*
        RdfUnmarshaller unmarshaller = new RdfUnmarshaller(ClassWithLangLiterals.class);
        
        ClassWithLangLiterals classWithLangLiterals = (ClassWithLangLiterals)unmarshaller
                                                                        .unmarshal(rdf_turtle);

        assertEquals(classWithLangLiterals.getSingleLiteral().getLanguage(), "en");
        assertEquals(classWithLangLiterals.getSingleLiteral().getString(), "test_literal_single");

        Literal first = ((ArrayList<Literal>)classWithLangLiterals.getLiteralCollection()).get(0);
        Literal second = ((ArrayList<Literal>)classWithLangLiterals.getLiteralCollection()).get(1);
        assertEquals(first.getLanguage(), "en");
        assertEquals(first.getString(), "test_literal_1");

        assertEquals(second.getLanguage(), "de");
        assertEquals(second.getString(), "test_literal_2");
        */
    }

}