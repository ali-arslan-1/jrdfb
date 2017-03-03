package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/* A temporal entity with non-zero extent or duration, i.e. for which the value of the beginning and end are different. */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#Interval")
public interface Interval extends TemporalEntity {

	// standard methods

	@RdfId
	java.net.URL getId();

	// accessor methods as derived from vocabulary

	/* Beginning of an Interval. */
	@RdfProperty("http://industrialdataspace.org/2016/10/ids/core#beginning")
	Instant getBeginning();

	/* End of an Interval. */
	@RdfProperty("http://industrialdataspace.org/2016/10/ids/core#end")
	Instant getEnd();
}
