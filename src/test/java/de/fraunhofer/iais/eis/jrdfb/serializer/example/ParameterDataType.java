package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfUri;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("http://industrialdataspace.org/2016/10/ids/core#ParameterDataType")
public enum ParameterDataType {
    @RdfUri("http://industrialdataspace.org/2016/10/ids/core/datatype#dateTime")
    XSD_DATETIME,

    @RdfUri("http://industrialdataspace.org/2016/10/ids/core/datatype#string")
    XSD_STRING,

    @RdfUri("http://industrialdataspace.org/2016/10/ids/core/datatype#bigint")
    XSD_INTEGER,
}