package de.fraunhofer.iais.eis.jrdfb.serializer;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Parameter;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ParameterDataType;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ParameterImpl;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ids.*;
import de.fraunhofer.iais.eis.jrdfb.util.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.GregorianCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class DatasetTest {

    RdfSerializer serializer;

    @Test
    public void testSerializeDataset() throws Exception {
        serializer = new RdfSerializer(Dataset.class, Instant.class, Interval.class);

        InstantImpl beginning = new InstantImpl();
        beginning.url = new URL("http://example.org/begin");
        beginning.inXSDDateTime = new XMLGregorianCalendarImpl(new GregorianCalendar());

        InstantImpl end = new InstantImpl();
        end.url = new URL("http://example.org/end");
        end.inXSDDateTime = new XMLGregorianCalendarImpl(new GregorianCalendar());

        IntervalImpl interval = new IntervalImpl();
        interval.url = new URL("http://example.org/interval");
        interval.beginning = beginning;
        interval.end = end;

        DatasetImpl dataset = new DatasetImpl();
        dataset.url = new URL("http://example.org/bla");
        dataset.coversTemporal = Arrays.asList(interval);

        String serializedTurtle = serializer.serialize(dataset).trim();

        System.out.println("Serialized Turtle:");
        System.out.println(serializedTurtle);

        Assert.fail("members seem not to be serialized properly");
    }

}