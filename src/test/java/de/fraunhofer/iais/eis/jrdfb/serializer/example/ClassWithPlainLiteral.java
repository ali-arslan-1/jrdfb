package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.datatype.PlainLiteral;
import org.apache.jena.rdf.model.Literal;

import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("example:ClassWithPlainLiteral")
public class ClassWithPlainLiteral {

    @RdfId
    private String id = "example.com/ClassWithPlainLiteral";

    @RdfProperty("example:singlePlainLiteral")
    private PlainLiteral singlePlainLiteral;

    public PlainLiteral getSinglePlainLiteral() {
        return singlePlainLiteral;
    }

    public void setSinglePlainLiteral(PlainLiteral singlePlainLiteral) {
        this.singlePlainLiteral = singlePlainLiteral;
    }
}
