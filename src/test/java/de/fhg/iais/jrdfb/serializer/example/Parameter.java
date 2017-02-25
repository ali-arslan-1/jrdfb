package de.fhg.iais.jrdfb.serializer.example;

import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.annotation.RdfType;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#Parameter")
public interface Parameter {
    @RdfProperty("http://industrialdataspace.org/2016/10/ids/core#dataType")
    ParameterDataType getDataType();
}
