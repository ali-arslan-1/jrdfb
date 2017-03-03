package de.fraunhofer.iais.eis.jrdfb.serializer.example.ids;

/* A temporal entity with non-zero extent or duration, i.e. for which the value of the beginning and end are different. */
public class IntervalImpl implements Interval {

	public java.net.URL url;

	// instance fields as derived from vocabulary

	 public Instant beginning;

	 public Instant end;




	// no manual construction
	public IntervalImpl() {}

	@Override
	final public java.net.URL getId() {
		return url;
	}

	// accessor method implementations as derived from vocabulary

	final public 
	
	Instant getBeginning() {
		return beginning;
	}

	final public 
	
	Instant getEnd() {
		return end;
	}



}
