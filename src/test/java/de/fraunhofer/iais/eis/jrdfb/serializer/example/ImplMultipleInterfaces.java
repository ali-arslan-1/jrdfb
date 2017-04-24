package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("example:ImplMultipleInterfaces")
public class ImplMultipleInterfaces implements Foo{
    @Override
    public String bar() {
        return "foo";
    }

    @Override
    public String foo() {
        return "bar";
    }
}
