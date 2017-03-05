package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfUri;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class EnumResolver extends ObjectResolver {

    public EnumResolver(Field field, RdfSerializer rdfSerializer) {
        super(field, rdfSerializer);
    }

    public EnumResolver(Method method, RdfSerializer rdfSerializer) {
        super(method, rdfSerializer);
    }

    @Override
    public @Nullable RDFNode resolveMember(@NotNull Object object)
            throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        if(value == null) return null;

        RDFNode rdfNode;
        RdfUri rdfUri = memberWrapper.getType().getField(((Enum)value).name())
                .getAnnotation(RdfUri.class);
        if(rdfUri != null){
            rdfNode =  model.createProperty(rdfUri.value());
        }else{
            rdfNode = model.createLiteral(value.toString());
        }

        return rdfNode;
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        String stringValue = null;
        if(value.getObject().isURIResource()){
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
