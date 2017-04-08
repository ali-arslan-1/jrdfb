package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/* Dataset on the Industrial Dataspace. May be a data offering, a subset of a dataset that is in the state of being transfered or an aggregation of multiple other datasets. */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#Dataset")
public interface Dataset {

	// standard methods

	@RdfId
	java.net.URL getUrl();

	// accessor methods as derived from vocabulary

	/* Temporal period or instance covered by the dataset. */
	@RdfProperty("http://industrialdataspace.org/2016/10/ids/core#coversTemporal")
	java.util.Collection<TemporalEntity> getCoversTemporal();

}
