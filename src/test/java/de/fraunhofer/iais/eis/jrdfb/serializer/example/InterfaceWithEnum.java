package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("http://exampleClass.org")
public interface InterfaceWithEnum {
    @RdfProperty("http://example.org/day")
    public DayEnum getSomeEnum();
}
