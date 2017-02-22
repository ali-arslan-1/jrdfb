package de.fhg.iais.jrdfb.serializer.example;

import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.annotation.RdfType;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("http://exampleClass.org")
public interface InterfaceWithEnum {
    @RdfProperty("http://example.org/day")
    public DayEnum getSomeEnum();
}
