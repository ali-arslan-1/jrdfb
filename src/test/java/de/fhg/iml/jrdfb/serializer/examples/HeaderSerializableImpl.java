/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhg.iml.jrdfb.serializer.examples;

import de.fhg.iml.ids.metadata.*;
import de.fhg.iml.ids.metadata.internal.StringTokenXMLAdapter;
import de.fhg.iml.ids.metadata.internal.URIBuilder;
import de.fhg.iml.ids.metadata.util.*;
import de.fhg.iml.jrdfb.annotation.*;
import de.fhg.iml.jrdfb.vocabulary.DcTerms;
import de.fhg.iml.jrdfb.vocabulary.IDS;
import de.fhg.iml.jrdfb.vocabulary.OWL;
import de.fhg.iml.jrdfb.vocabulary.VOID;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class implements both the header and the header builder interface.
 * 
 * @author <a href="mailto:bernd.breitenbach@iml.fraunhofer.de">Bernd Breitenbach</a>
 */
@XmlRootElement(name = "metaData")
@XmlAccessorType(XmlAccessType.PROPERTY)
@RdfType(IDS.TRANSFERED_DATASET)
public class HeaderSerializableImpl implements Header, HeaderBuilder {

    @RdfId(baseURI = "http://industrialdataspace.org/instance/header/")
    @RdfProperty(property = DcTerms.IDENTIFIER)
	private UUID id;

    @RdfProperty(property = OWL.VERSION_INFO)
	private byte version = 1;

    @RdfProperty(property = IDS.SENDER, path = "address")
	private Sender sender;

    @RdfProperty(property = IDS.RECEIVER, path = "address")
	private Receiver receiver;

    @RdfProperty(property = IDS.DIGEST)
	private String payloadDigest;

    @RdfProperty(property = IDS.AUTH_TOKEN)
	private AuthToken authorizationToken;

    @RdfProperty(property = IDS.CREATED_AT)
    @RdfTypedLiteral("xsd:dateTime")
	private LocalDateTime creationTime;

    @RdfProperty(property = IDS.CUSTOM_PROPERTY)
    @RdfBag
	private SortedMap<String, String> customAttributes;

    @RdfProperty(property = IDS.FORMAT)
	private String format;

    @RdfProperty(property = VOID.VOCABULARY)
	private URI vocabulary;

	private MessageDigest digestCalculator;

	private boolean readOnly=false;

	/**
	 * Get the value of readOnly
	 *
	 * @return the value of readOnly
	 */
	@XmlTransient
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Set the value of readOnly
	 *
	 * @param readOnly new value of readOnly
	 */
	private void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	@XmlJavaTypeAdapter(SenderXmlAdapter.class)
	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	@Override
	@XmlJavaTypeAdapter(ReceiverXmlAdapter.class)
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@Override
    public String getPayloadDigest() {
		return payloadDigest;
	}

	public void setPayloadDigest(String payloadDigest) {
		this.payloadDigest = payloadDigest;
	}

	@Override
	@XmlJavaTypeAdapter(StringTokenXMLAdapter.class)
	public AuthToken getAuthorizationToken() {
		return authorizationToken;
	}

	public void setAuthorizationToken(AuthToken authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	@Override
	@XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the customAttributes
	 */
	@XmlJavaTypeAdapter(MapXmlAdapter.class)
	@XmlElement(name = "customProperties")
	public SortedMap<String, String> getCustomAttributes() {
		return customAttributes;
	}

	/**
	 * @param customAttributes the customAttributes to set
	 */
	public void setCustomAttributes(SortedMap<String, String> customAttributes) {
		this.customAttributes = customAttributes;
	}

	@Override
	public HeaderBuilder customAttribute(String key, String value) {
		assertModifiable();
		if (getCustomAttributes() == null) {
			setCustomAttributes(new TreeMap<>());
		}
		getCustomAttributes().put(key, value);
		return this;
	}

	public String customAttribute(String key) {
		if (getCustomAttributes() == null) {
			return null;
		}
		return getCustomAttributes().get(key);
	}

	/**
	 * @return the version
	 */
	@XmlAttribute()
	public byte getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(byte version) {
		this.version = version;
	}

	@Override
	public HeaderBuilder sender(Sender sender) {
		assertModifiable();
		if (sender==null) {
			throw new NullPointerException("sender cannot be null");
		}
		setSender(sender);
		return this;
	}

	@Override
	public HeaderBuilder receiver(Receiver receiver) {
		assertModifiable();
		if (receiver==null) {
			throw new NullPointerException("receiver cannot be null");
		}
		setReceiver(receiver);
		return this;
	}

	@Override
	public HeaderBuilder digestPayload(byte[] payload) {
		assertModifiable();
		getDigestCalculator().reset();
		byte digest[] = getDigestCalculator().digest(payload);
		setPayloadDigest(Base64.getEncoder().encodeToString(digest));
		return this;
	}

	@Override
	public HeaderBuilder creationTime(LocalDateTime created) {
		assertModifiable();
		setCreationTime(created);
		return this;
	}

	/**
	 * @return the digestCalculator
	 */
	@XmlAttribute(name = "hashAlgorithm")
	@XmlJavaTypeAdapter(DigestCalculatorXmlAdapter.class)
	protected MessageDigest getDigestCalculator() {
		return digestCalculator;
	}

	/**
	 * @param digestCalculator the digestCalculator to set
	 */
	protected void setDigestCalculator(MessageDigest digestCalculator) {
		this.digestCalculator = digestCalculator;
	}

	public SortedMap<String, String> customAttributeMap() {
		return Collections.unmodifiableSortedMap(getCustomAttributes());
	}

	@Override
	public HeaderBuilder authorizationToken(AuthToken token) {
		assertModifiable();
		setAuthorizationToken(token);
		return this;
	}

	@Override
	public Header build() {
		if (isReadOnly()) {
			throw new RuntimeException("object was already built");
		}
		setReadOnly(true);
		return this;
	}

	@Override
	public HeaderBuilder customAttributeMap(Map<String, String> map) {
		assertModifiable();
		if (getCustomAttributes() == null) {
			setCustomAttributes(new TreeMap<>());
		} else {
			getCustomAttributes().clear();
		}
		for (Map.Entry<String, String> e : map.entrySet()) {
			customAttribute(e.getKey(), e.getValue());
		}
		return this;
	}

	@Override
	public HeaderBuilder format(String format) {
		assertModifiable();
		setFormat(format);
		return this;
	}

	@Override
	public HeaderBuilder vocabulary(String vocabulary) {
		assertModifiable();
		setVocabulary(URIBuilder.fromString(vocabulary));
		return this;
	}
	
	@Override
	public HeaderBuilder vocabulary(URI vocabulary) {
		// just to perform an URI check
		return vocabulary(vocabulary.toString());
	}

	/**
	 * @return the format
	 */
	@Override
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the vocabulary
	 */
	@Override
	public URI getVocabulary() {
		return vocabulary;
	}

	/**
	 * @param vocabulary the vocabulary to set
	 */
	public void setVocabulary(URI vocabulary) {
		this.vocabulary = vocabulary;
	}

	private void assertModifiable() {
		if (isReadOnly()) {
			throw new RuntimeException("object is built and cannot be modified");
		}
	}
}
