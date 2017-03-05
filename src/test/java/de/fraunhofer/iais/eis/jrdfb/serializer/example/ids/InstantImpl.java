package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

/* A temporal entity with zero extent or duration. */
public class InstantImpl implements Instant {

	public java.net.URL url;

	// instance fields as derived from vocabulary

	public javax.xml.datatype.XMLGregorianCalendar inXSDDateTime;




	// no manual construction
	public InstantImpl() {}

	@Override
	final public java.net.URL getId() {
		return url;
	}

	// accessor method implementations as derived from vocabulary


	final public
	javax.xml.datatype.XMLGregorianCalendar getInXSDDateTime() {
		return inXSDDateTime;
	}



}
