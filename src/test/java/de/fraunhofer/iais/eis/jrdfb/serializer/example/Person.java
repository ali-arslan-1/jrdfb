package de.fraunhofer.iais.eis.jrdfb.serializer.example;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.VCARD;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@RdfType(VCARD.INDIVIDUAL)
public class Person
{
    @RdfProperty(VCARD.FN)
    private String name;

    @RdfProperty(VCARD.HAS_ADDRESS)
	private Address address;

    @RdfProperty(VCARD.BDAY)
	private XMLGregorianCalendar birthDate;

	private List<Person> friends;

    public Person(String name){
        this.name = name;
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
}

