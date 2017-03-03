package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/* A temporal entity with zero extent or duration. */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#Instant")
public interface Instant extends TemporalEntity {

	// standard methods

	@RdfId
	java.net.URL getId();

	// accessor methods as derived from vocabulary

	/* Position of an Instant. */
	@RdfProperty("http://industrialdataspace.org/2016/10/ids/core#inXSDDateTime")
	javax.xml.datatype.XMLGregorianCalendar getInXSDDateTime();
}
