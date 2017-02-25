package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#Parameter")
public interface Parameter {
    @RdfProperty("http://industrialdataspace.org/2016/10/ids/core#dataType")
    ParameterDataType getDataType();
}
