package de.fhg.iais.jrdfb.serializer.example;

import de.fhg.iais.jrdfb.annotation.RdfId;
import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.annotation.RdfType;
import de.fhg.iais.jrdfb.vocabulary.VCARD;

import java.io.Serializable;
import java.net.URL;

@RdfType(VCARD.ADDRESS)
public class Address implements Addressable, Serializable
{

	@RdfId
	private URL url;

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

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}

