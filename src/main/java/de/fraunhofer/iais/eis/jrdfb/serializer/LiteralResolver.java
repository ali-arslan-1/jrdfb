package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfTypedLiteral;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Constructor;
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
public class LiteralResolver extends ObjectResolver {

    public LiteralResolver(Field field, RdfSerializer rdfSerializer) {
        super(field, rdfSerializer);
    }

    public LiteralResolver(Method method, RdfSerializer rdfSerializer) {
        super(method, rdfSerializer);
    }

    @Override
    public RDFNode resolveMember(@NotNull Object object) throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        if(value == null) return null;
        RDFNode rdfNode;

        if(memberWrapper.isAnnotationPresent(RdfTypedLiteral.class)){
            rdfNode =  model.createTypedLiteral(value.toString(),
                memberWrapper.getAnnotation(RdfTypedLiteral.class).value());
        } else if(LiteralMapping.containsKey(memberWrapper.getType())){
            rdfNode =  model.createTypedLiteral(value.toString(),
                                                    LiteralMapping.get(memberWrapper.getType()));
        }else if(memberWrapper.getType().equals(URL.class)){
            rdfNode =  model.createProperty(value.toString());
        }else if(RDFNode.class.isAssignableFrom(memberWrapper.getType())){
            rdfNode = (RDFNode)value;
        }
        else{
            rdfNode = model.createLiteral(value.toString());
        }

        return rdfNode;
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(value==null)return null;
        if(getMemberPath().isEmpty()){
            String stringValue;
            if(memberWrapper.getType().equals(URL.class))
                stringValue = value.getObject().toString();
            else if(RDFNode.class.isAssignableFrom(memberWrapper.getType())){
                return value.getObject();
            }
            else
                stringValue = value.getLiteral().getString();
            return ReflectUtils.stringToObject(resolveMemberClassName(resource),
                    stringValue);
        }else{
            Constructor cons = Class.forName(
                    memberWrapper.getDeclaringClass().getName())
                    .getDeclaredConstructor();
            cons.setAccessible(true);
            return memberWrapper.initNestedObject(cons.newInstance(),
                    getMemberPath(),
                    value.getObject().toString());
        }
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
