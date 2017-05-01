package de.fraunhofer.iais.eis.jrdfb.serializer;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Address;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Person;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.Student;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.ids.*;
import org.testng.annotations.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MultipleSerializeDeserializeTest {

    RdfMarshaller marshaller = new RdfMarshaller(Dataset.class, Instant.class, Interval.class,
            TemporalEntity.class, Person.class, Student.class, Address.class);

    RdfUnmarshaller unmarshaller = new RdfUnmarshaller(Dataset.class, Instant.class, Interval.class,
            TemporalEntity.class, Person.class, Student.class, Address.class);

    private Dataset createDataset() throws Exception {
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

        return dataset;
    }

    private Student createStudent() throws DatatypeConfigurationException, MalformedURLException {
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
        return student;
    }

    @Test
    public void testDeserializeDataset() throws Exception{
        String serializedDataset = marshaller.marshal(createDataset());
        String serializedStudent = marshaller.marshal(createStudent());

        System.out.println("serializedDataset:");
        System.out.println(serializedDataset);
        System.out.println("serializedStudent:");
        System.out.println(serializedStudent);

        Dataset dataset = (Dataset) unmarshaller.unmarshal(serializedDataset);
        assertNotNull(dataset);

        ArrayList<IntervalImpl> interval = (ArrayList) dataset.getCoversTemporal();
        assertEquals(interval.get(0).getBeginning().getInXSDDateTime().getYear(), 2017);
        assertEquals(interval.get(0).getBeginning().getInXSDDateTime().getMonth(), 2);
        assertEquals(interval.get(0).getBeginning().getInXSDDateTime().getDay(), 1);

        assertEquals(interval.get(0).getEnd().getInXSDDateTime().getYear(), 2017);
        assertEquals(interval.get(0).getEnd().getInXSDDateTime().getMonth(), 2);
        assertEquals(interval.get(0).getEnd().getInXSDDateTime().getDay(), 2);

        Student student = (Student) unmarshaller.unmarshal(serializedStudent);

        assertEquals(student.getName(), "Ali Arslan");
        assertEquals(student.getMatrNo().intValue(), 111111);

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