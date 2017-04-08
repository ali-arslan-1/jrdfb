package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

/* Dataset on the Industrial Dataspace. May be a data offering, a subset of a dataset that is in the state of being transfered or an aggregation of multiple other datasets. */
public class DatasetImpl implements Dataset {

	public java.net.URL url;

	// instance fields as derived from vocabulary

	public java.util.Collection<TemporalEntity> coversTemporal;

	// no manual construction
	public DatasetImpl() {}

	@Override
	final public java.net.URL getUrl() {
		return url;
	}

	// accessor method implementations as derived from vocabulary

	final public
	java.util.Collection<TemporalEntity> getCoversTemporal() {
		return coversTemporal;
	}

}
