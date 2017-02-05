package de.fhg.iais.jrdfb.serializer.example;


import de.fhg.iais.jrdfb.annotation.RdfProperty;

public interface Addressable
{
	@RdfProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lat")
	public double getLatitude() ;

    @RdfProperty("http://www.w3.org/2003/01/geo/wgs84_pos#long")
	public double getLongitude() ;

}

