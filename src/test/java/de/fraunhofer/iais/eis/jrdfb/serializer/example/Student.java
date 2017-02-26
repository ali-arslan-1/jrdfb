package de.fraunhofer.iais.eis.jrdfb.serializer.example;


import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.DcTerms;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.VCARD;

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

    public Student(String name, String ssn) {
        super(name, ssn);
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

