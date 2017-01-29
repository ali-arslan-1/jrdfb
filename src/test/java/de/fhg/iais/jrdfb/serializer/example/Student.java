package de.fhg.iais.jrdfb.serializer.example;


import de.fhg.iais.jrdfb.annotation.RdfId;
import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.vocabulary.DcTerms;

public class Student extends Person
{
    @RdfProperty(DcTerms.IDENTIFIER)
    @RdfId(uriTemplate = "student/{RdfId}")
    private int matrNo;

	public Student(){
		super();
	}

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
}

