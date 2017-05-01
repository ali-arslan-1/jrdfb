package de.fraunhofer.iais.eis.jrdfb.serializer;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ids.*;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class DatasetTest {
    
    String rdf_turtle;
    Model expectedModel;

    @BeforeMethod
    public void setUp() throws Exception {
        rdf_turtle = FileUtils
                .readResource("Dataset.ttl",
                        this.getClass());
        expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(new ByteArrayInputStream(rdf_turtle.getBytes()), null, "TURTLE");
    }

    @Test
    public void testSerializeDataset() throws Exception {
        RdfMarshaller marshaller = new RdfMarshaller(Dataset.class, Instant.class, Interval.class,
                TemporalEntity.class);

        GregorianCalendar t1 = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        t1.set(2017, 1, 1, 0, 0, 0);
        t1.set(Calendar.MILLISECOND, 0);

        GregorianCalendar t2 = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        t2.set(2017, 1, 2, 0, 0, 0);
        t2.set(Calendar.MILLISECOND, 0);

        InstantImpl beginning = new InstantImpl();
        beginning.url = new URL("http://example.org/begin");
        beginning.inXSDDateTime = new XMLGregorianCalendarImpl(t1);

        InstantImpl end = new InstantImpl();
        end.url = new URL("http://example.org/end");
        end.inXSDDateTime = new XMLGregorianCalendarImpl(t2);

        IntervalImpl interval = new IntervalImpl();
        interval.url = new URL("http://example.org/interval");
        interval.beginning = beginning;
        interval.end = end;

        DatasetImpl dataset = new DatasetImpl();
        dataset.url = new URL("http://example.org/bla");
        dataset.coversTemporal = Arrays.asList(interval);

        String serializedTurtle = marshaller.marshal(dataset).trim();
        Model actualModel = ModelFactory.createDefaultModel();
        actualModel.read(new ByteArrayInputStream(serializedTurtle.getBytes()),
                null, "TURTLE");
        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);


        assertTrue(expectedModel.isIsomorphicWith(actualModel));
    }

    @Test
    public void testDeserializeDataset() throws Exception{
        RdfUnmarshaller marshaller = new RdfUnmarshaller(Dataset.class, Instant.class, Interval
                .class,
                TemporalEntity.class);

        Dataset dataset = (Dataset) marshaller.unmarshal(rdf_turtle);
        assertNotNull(dataset);

        ArrayList<IntervalImpl> interval = (ArrayList) dataset.getCoversTemporal();
        assertEquals(interval.get(0).getBeginning().getInXSDDateTime().getYear(), 2017);
        assertEquals(interval.get(0).getBeginning().getInXSDDateTime().getMonth(), 2);
        assertEquals(interval.get(0).getBeginning().getInXSDDateTime().getDay(), 1);

        assertEquals(interval.get(0).getEnd().getInXSDDateTime().getYear(), 2017);
        assertEquals(interval.get(0).getEnd().getInXSDDateTime().getMonth(), 2);
        assertEquals(interval.get(0).getEnd().getInXSDDateTime().getDay(), 2);


    }
}