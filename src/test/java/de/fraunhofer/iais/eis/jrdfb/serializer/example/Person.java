package de.fraunhofer.iais.eis.jrdfb.serializer.example;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.DcTerms;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.VCARD;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@RdfType(VCARD.INDIVIDUAL)
public class Person
{
	@RdfProperty(DcTerms.IDENTIFIER)
	@RdfId(uriTemplate = "person/{RdfId}")
	private String ssn;

    @RdfProperty(VCARD.FN)
    private String name;

    @RdfProperty(VCARD.HAS_ADDRESS)
	private Address address;

    @RdfProperty(VCARD.BDAY)
	private XMLGregorianCalendar birthDate;

    @RdfProperty("example:hasFriends")
	private List<Person> friends;

    public Person(String name){
        this.name = name;
    }

    public Person(String name, String ssn){
        this.name = name;
        this.ssn = ssn;
    }

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Person> getFriends() {
		return friends;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public XMLGregorianCalendar getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(XMLGregorianCalendar birthDate) {
		this.birthDate = birthDate;
	}

	public String getSsn() {
		return ssn;
	}
}

