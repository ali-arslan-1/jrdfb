package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfUri;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class EnumUnmarshaller extends BasePropUnmarshaller {

    public EnumUnmarshaller(Field field, RdfSerializer rdfSerializer) {
        super(field, rdfSerializer);
    }

    public EnumUnmarshaller(Method method, RdfSerializer rdfSerializer) {
        super(method, rdfSerializer);
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        String stringValue = null;
        if(value != null && value.getObject().isURIResource()){
            String uri = value.getObject().toString();
            Field [] enumValues = memberWrapper.getType().getDeclaredFields();
            for (Field enumVal : enumValues){
                RdfUri rdfUri  = enumVal.getAnnotation(RdfUri.class);
                if(rdfUri != null && rdfUri.value().equals(uri)){
                    stringValue = enumVal.getName();
                }
            }
            return ReflectUtils.stringToObject(resolveMemberClassName(resource),
                    stringValue);
        }
        return null;
    }
}
