package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Parameter;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ParameterDataType;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ParameterImpl;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.RdfUnmarshaller;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MarshalWithStreamsTest {

    RdfMarshaller marshaller;
    RdfUnmarshaller unmarshaller;
    Model expectedModel;


    @BeforeMethod
    public void setUp() throws Exception {

        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(FileManager.get()
                        .open("de/fraunhofer/iais/eis/jrdfb/serializer/ParameterImpl.ttl"), null,
                "TURTLE");
    }

    @Test
    public void testSerialize() throws Exception {
        marshaller = new RdfMarshaller(ParameterImpl.class);
        Model actualModel = createSerializedParameterModel();
        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    private Model createSerializedParameterModel() throws Exception{
        Parameter parameter = new ParameterImpl(ParameterDataType.XSD_STRING);

        Model actualModel = ModelFactory.createDefaultModel();
        FileOutputStream out = new FileOutputStream("target/temp.ttl");
        marshaller.marshal(parameter, out);

        System.out.println("Serialized Turtle:");
        System.out.println(out.toString());
        out.close();

        FileInputStream in = new FileInputStream("target/temp.ttl");
        actualModel.read(in, null, "TURTLE");
        in.close();
        return actualModel;
    }

    @Test
    public void testDeserialize() throws Exception{
        unmarshaller = new RdfUnmarshaller(ParameterImpl.class);
        ParameterImpl parameter =
                (ParameterImpl) unmarshaller
                        .unmarshal(FileManager.get()
                        .open("de/fraunhofer/iais/eis/jrdfb/serializer/ParameterImpl.ttl"));
        assertEquals(parameter.getDataType(), ParameterDataType.XSD_STRING);
    }
}