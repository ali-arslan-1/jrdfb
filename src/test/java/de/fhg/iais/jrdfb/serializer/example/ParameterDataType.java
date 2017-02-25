package de.fhg.iais.jrdfb.serializer.example;

import de.fhg.iais.jrdfb.annotation.RdfType;
import de.fhg.iais.jrdfb.annotation.RdfUri;

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