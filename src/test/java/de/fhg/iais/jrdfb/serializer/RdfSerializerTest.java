package de.fhg.iais.jrdfb.serializer;

import de.fhg.iais.jrdfb.util.FileUtils;
import de.fhg.iais.jrdfb.vocabulary.IDS;
import de.fhg.iml.ids.metadata.Addressable;
import de.fhg.iml.ids.metadata.impl.HeaderSerializableImpl;
import de.fhg.iml.ids.metadata.impl.StringToken;
import de.fhg.iml.ids.metadata.internal.URIBuilder;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfSerializerTest {
    RdfSerializer serializer;
    String rdf_turtle;

    public RdfSerializerTest() throws IOException {
        serializer = new RdfSerializer(HeaderSerializableImpl.class);
        rdf_turtle = FileUtils
                .readResource("transferedDataset_test_all.ttl",
                        this.getClass());
    }

    @Test
    public void testSerialize() throws Exception {

        HeaderSerializableImpl metaDataMock = new HeaderSerializableImpl();

        metaDataMock.setId(UUID.fromString("12345678-1234-1234-1234-123456789012"));
        metaDataMock.setSender(new Addressable("https://www.iml.frauhofer.de"));
        metaDataMock.setReceiver(new Addressable("http://fraunhofer.de"));
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


        assertEquals(serializer.serialize(metaDataMock).trim(), rdf_turtle.trim());
    }

    @Test
    public void testDeserializeMap() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals(actualHeader.customAttribute("myparameter"), "ok");

    }


    @Test
    public void testDeserializeStringValues() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals("authorized!", actualHeader.getAuthorizationToken().toString());

        String expectedDigest = Base64.getEncoder().encodeToString(MessageDigest
                .getInstance("SHA-512")
                .digest(new byte[]{1, 2, 3}));

        assertEquals(actualHeader.getPayloadDigest(), expectedDigest);
    }

    @Test
    public void testDeserializeLocalDateTime() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals(actualHeader.getCreationTime(), LocalDateTime.of(2016, 11, 8, 12, 30));
    }

    @Test
    public void testDeserializeNestedObjects() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals("https://www.iml.frauhofer.de", actualHeader.getSender()
                .getAddress().toString());
        assertEquals(actualHeader.getReceiver()
                .getAddress().toString(), "http://fraunhofer.de");
    }

    @Test
    public void testDeserializeUUID() throws Exception {

        HeaderSerializableImpl actualHeader = (HeaderSerializableImpl)serializer
                .deserialize(rdf_turtle);

        assertEquals(actualHeader.getId(), UUID.fromString("12345678-1234-1234-1234-123456789012"));
    }

}