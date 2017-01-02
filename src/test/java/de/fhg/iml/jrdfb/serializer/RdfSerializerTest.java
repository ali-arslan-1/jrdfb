package de.fhg.iml.jrdfb.serializer;

import de.fhg.iml.ids.metadata.Receiver;
import de.fhg.iml.ids.metadata.Sender;
import de.fhg.iml.ids.metadata.impl.StringToken;
import de.fhg.iml.ids.metadata.internal.URIBuilder;
import de.fhg.iml.jrdfb.serializer.examples.*;
import de.fhg.iml.jrdfb.util.FileUtils;
import de.fhg.iml.jrdfb.vocabulary.IDS;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import static org.testng.Assert.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfSerializerTest {
    RdfSerializer serializer;
    String rdf_turtle;

    public RdfSerializerTest() {
        serializer = new RdfSerializer<>(HeaderSerializableImpl.class);
        rdf_turtle = FileUtils
                .readFile("src/test/resources/data/transferedDataset_test_all.ttl",
                        StandardCharsets.UTF_8);
    }

    @Test
    public void testSerialize() throws NoSuchAlgorithmException, IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException, NoSuchFieldException {

        HeaderSerializableImpl metaDataMock = new HeaderSerializableImpl();

        metaDataMock.setId(UUID.fromString("12345678-1234-1234-1234-123456789012"));
        metaDataMock.setSender(new Sender("https://www.iml.frauhofer.de"));
        metaDataMock.setReceiver(new Receiver("http://fraunhofer.de"));
        metaDataMock.setAuthorizationToken(new StringToken("authorized!"));
        byte version = 2;
        metaDataMock.setVersion(version);
        metaDataMock.setVocabulary(URIBuilder.fromString(IDS.getURI()));
        metaDataMock.setFormat("rdf");

        String digestMock = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-512")
                .digest(new byte[]{1, 2, 3}));
        metaDataMock.setPayloadDigest(digestMock);

        metaDataMock.setCreationTime(LocalDateTime.of(2016, 11, 8, 12, 30));

        SortedMap<String, String> attributesMock = new TreeMap<>();
        attributesMock.put("myparameter", "ok");

        metaDataMock.setCustomAttributes(attributesMock);


        assertEquals( rdf_turtle.trim() , serializer.serialize(metaDataMock).trim());
    }

    @Test
    public void testDeserializeMap() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals("ok" , actualHeader.customAttribute("myparameter"));

    }


    @Test
    public void testDeserializeStringValues() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals("authorized!", actualHeader.getAuthorizationToken().toString());

        String expectedDigest = Base64.getEncoder().encodeToString(MessageDigest
                .getInstance("SHA-512")
                .digest(new byte[]{1, 2, 3}));

        assertEquals(expectedDigest , actualHeader.getPayloadDigest());
    }

    @Test
    public void testDeserializeLocalDateTime() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals(LocalDateTime.of(2016, 11, 8, 12, 30) , actualHeader.getCreationTime());
    }

    @Test
    public void testDeserializeNestedObjects() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals("https://www.iml.frauhofer.de", actualHeader.getSender()
                .getAddress().toString());
        assertEquals("http://fraunhofer.de", actualHeader.getReceiver()
                .getAddress().toString());
    }

    @Test
    public void testDeserializeUUID() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals(UUID.fromString("12345678-1234-1234-1234-123456789012"), actualHeader.getId());
    }

}