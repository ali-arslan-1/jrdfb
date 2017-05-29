package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfTypedLiteral;
import org.apache.jena.ext.com.google.common.base.Charsets;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.NotNull;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class LiteralMarshaller extends BasePropMarshaller {

    public LiteralMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        super(field, rdfMarshaller);
    }

    public LiteralMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        super(method, rdfMarshaller);
    }

    @Override
    public RDFNode resolveMember(@NotNull Object object, Model model)
            throws ReflectiveOperationException {
        Object value = memberWrapper.extractMemberValue(object);
        if(value == null) return null;
        RDFNode rdfNode;

        if(memberWrapper.isAnnotationPresent(RdfTypedLiteral.class)){
            rdfNode =  model.createTypedLiteral(value.toString(),
                memberWrapper.getAnnotation(RdfTypedLiteral.class).value());
        } else if(LiteralMapping.containsKey(memberWrapper.getType())){
            String serializedValue = value.toString();
            if(LiteralMapping.get(memberWrapper.getType())
                    .equals("xsd:base64Binary"))
                serializedValue = new String((byte[])value, Charsets.UTF_8);

            rdfNode =  model.createTypedLiteral(
                    serializedValue,
                    LiteralMapping.get(memberWrapper.getType())
            );
        }else if(memberWrapper.getType().equals(URL.class)){
            rdfNode =  model.createProperty(value.toString());
        }else if(RDFNode.class.isAssignableFrom(memberWrapper.getType())){
            rdfNode = (RDFNode)value;
        }else{
            rdfNode = model.createLiteral(value.toString());
        }

        return rdfNode;
    }

    private static final Map<Type, String> LiteralMapping;
    static
    {
        LiteralMapping = new HashMap<>();
        LiteralMapping.put(XMLGregorianCalendar.class, "xsd:dateTime");
        LiteralMapping.put(GregorianCalendar.class, "xsd:dateTime");
        LiteralMapping.put(Date.class, "xsd:dateTime");
        LiteralMapping.put(BigInteger.class, "xsd:integer");
        LiteralMapping.put(int.class, "xsd:int");
        LiteralMapping.put(Integer.class, "xsd:int");
        LiteralMapping.put(long.class, "xsd:long");
        LiteralMapping.put(Long.class, "xsd:long");
        LiteralMapping.put(short.class, "xsd:short");
        LiteralMapping.put(Short.class, "xsd:short");
        LiteralMapping.put(float.class, "xsd:float");
        LiteralMapping.put(Float.class, "xsd:float");
        LiteralMapping.put(double.class, "xsd:long");
        LiteralMapping.put(Double.class, "xsd:long");
        LiteralMapping.put(boolean.class, "xsd:boolean");
        LiteralMapping.put(Boolean.class, "xsd:boolean");
        LiteralMapping.put(byte.class, "xsd:byte");
        LiteralMapping.put(Byte.class, "xsd:byte");
        LiteralMapping.put(byte[].class, "xsd:base64Binary");
        LiteralMapping.put(Byte[].class, "xsd:base64Binary");
    }
}
