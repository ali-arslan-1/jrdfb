package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfTypedLiteral;
import org.apache.jena.rdf.model.Literal;

import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("example:ClassWithByteArray")
public class ClassWithByteArray {

    @RdfId
    private String id = "example.com/classWithByteArray";

    @RdfProperty("example:byteArrayLiteral")
    private byte[] byteArrayLiteral;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getByteArrayLiteral() {
        return byteArrayLiteral;
    }

    public void setByteArrayLiteral(byte[] byteArrayLiteral) {
        this.byteArrayLiteral = byteArrayLiteral;
    }
}
