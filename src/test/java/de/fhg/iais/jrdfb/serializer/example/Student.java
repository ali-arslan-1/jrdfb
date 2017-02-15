package de.fhg.iais.jrdfb.serializer.example;


import de.fhg.iais.jrdfb.annotation.RdfId;
import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.vocabulary.DcTerms;
import de.fhg.iais.jrdfb.vocabulary.VCARD;

import java.net.URL;

public class Student extends Person
{
    @RdfProperty(DcTerms.IDENTIFIER)
    @RdfId(uriTemplate = "student/{RdfId}")
    private int matrNo;

    @RdfProperty(VCARD.URL)
    private URL profileUrl;

    public Student(String name, int matrNo) {
        super(name);
        this.matrNo = matrNo;
    }

    public int getMatrNo() {
        return matrNo;
    }

    public void setMatrNo(int matrNo) {
        this.matrNo = matrNo;
    }

    public URL getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(URL profileUrl) {
        this.profileUrl = profileUrl;
    }
}

