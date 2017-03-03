package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/* A temporal interval or instant. */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#TemporalEntity")
public interface TemporalEntity {

	// standard methods

	@RdfId
	java.net.URL getId();

	// accessor methods as derived from vocabulary

}
