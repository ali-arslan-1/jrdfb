package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import org.apache.jena.rdf.model.Literal;

import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("example:ClassWithLangLiterals")
public class ClassWithLangLiterals {

    @RdfId
    private String id = "example.com/classWithLangLiterals";

    @RdfProperty("example:singleLiteral")
    private Literal singleLiteral;

    @RdfProperty("example:literalCollection")
    private Collection<Literal> literalCollection;

    public Literal getSingleLiteral() {
        return singleLiteral;
    }

    public void setSingleLiteral(Literal singleLiteral) {
        this.singleLiteral = singleLiteral;
    }

    public Collection<Literal> getLiteralCollection() {
        return literalCollection;
    }

    public void setLiteralCollection(Collection<Literal> literalCollection) {
        this.literalCollection = literalCollection;
    }
}
