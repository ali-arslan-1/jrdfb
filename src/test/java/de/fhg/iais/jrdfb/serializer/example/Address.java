package de.fhg.iais.jrdfb.serializer.example;

import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.vocabulary.VCARD;

public class Address implements Addressable
{

    @RdfProperty(VCARD.LOCALITY)
	private String city;

    @RdfProperty(VCARD.COUNTRY_NAME)
	private String country;

    @RdfProperty(VCARD.STREET_ADDRESS)
	private String street;

    public Address() {
    }

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

    public double getLat() {
		// TODO implement me
		return 0.0;
	}

	public double getLong() {
		// TODO implement me
		return 0.0;
	}

}

