package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class LiteralUnmarshaller extends BasePropUnmarshaller {

    public LiteralUnmarshaller(Field field, RdfUnmarshaller rdfUnmarshaller) {
        super(field, rdfUnmarshaller);
    }

    public LiteralUnmarshaller(Method method, RdfUnmarshaller rdfUnmarshaller) {
        super(method, rdfUnmarshaller);
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

}
