package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.VCARD;

import java.io.Serializable;
import java.net.URL;

@RdfType(VCARD.ADDRESS)
public class Address implements Addressable, Serializable
{

	@RdfId
	private URL mapUrl;

	private double longitude;
	private double latitude;

    @RdfProperty(VCARD.LOCALITY)
	private String city;

    @RdfProperty(VCARD.COUNTRY_NAME)
	private String country;

    @RdfProperty(VCARD.STREET_ADDRESS)
	private String street;

    public Address(String city, String country){
		super();
		this.city = city;
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public URL getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(URL mapUrl) {
		this.mapUrl = mapUrl;
	}
}

