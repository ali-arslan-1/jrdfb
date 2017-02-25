package de.fraunhofer.iais.eis.jrdfb.serializer.example;


import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;

public interface Addressable
{
	@RdfProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lat")
	public double getLatitude() ;

    @RdfProperty("http://www.w3.org/2003/01/geo/wgs84_pos#long")
	public double getLongitude() ;

}

