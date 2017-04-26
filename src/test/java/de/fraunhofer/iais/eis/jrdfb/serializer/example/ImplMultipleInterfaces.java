package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("example:ImplMultipleInterfaces")
public class ImplMultipleInterfaces implements Foo{

    private String foo;
    private String bar;

    public ImplMultipleInterfaces(String foo, String bar) {
        this.foo = foo;
        this.bar = bar;
    }
    @Override
    public String getBar() {
        return bar;
    }

    @Override
    public String getFoo() {
        return foo;
    }
}
