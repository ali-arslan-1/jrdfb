package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VOID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public abstract class ObjectResolver implements Resolver {
    protected MemberWrapper memberWrapper;
    protected Model model;
    protected RdfSerializer rdfSerializer;

    public ObjectResolver(Field field, RdfSerializer rdfSerializer) {
        this.memberWrapper = new MemberWrapper(field);
        this.model = rdfSerializer.model;
        this.rdfSerializer = rdfSerializer;
    }

    public ObjectResolver(Method method, RdfSerializer rdfSerializer) {
        this.memberWrapper = new MemberWrapper(method);
        this.model = rdfSerializer.model;
        this.rdfSerializer = rdfSerializer;
    }

    protected Object extractMemberValue(Object object)
            throws ReflectiveOperationException {
        if(getMemberPath().isEmpty()){
            return getMemberValue(object);
        }else{
            return memberWrapper.getNestedObject(object, getMemberPath());
        }
    }

    public @Nullable Object getMemberValue(@NotNull Object object)
            throws ReflectiveOperationException {
        return memberWrapper.getValue(object);
    }

    public void setMemberValue(@NotNull Object object, @NotNull Object value)
            throws ReflectiveOperationException {
        if(!ClassUtils.isAssignable(value.getClass(), memberWrapper.getType())) return;
        memberWrapper.setValue(object, value);
    }

    @Nullable
    @Override
    public String resolveMemberClassName(@NotNull Object object) throws ReflectiveOperationException {
        Object extractedValue = extractMemberValue(object);
        if(extractedValue == null) return null;
        return extractedValue.getClass().getName();
    }

    @Override
    @Nullable
    public String resolveMemberClassName(@NotNull Resource resource)
            throws ReflectiveOperationException{
        Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();
        Statement metaProperty = metadata.getProperty(getJenaProperty());
        if(metaProperty==null)
            return null;
//            throw new NoSuchFieldException("Metadata for memberWrapper '"
//                    + memberWrapper.getName()+"' Not provided" +
//                    " in RDF Resource: "+resource.getURI());
        return metaProperty.getObject().toString();
    }

    protected Property getJenaProperty(){
        return model.createProperty(getRdfProperty().value());
    }

    protected RdfProperty getRdfProperty(){
        return memberWrapper.getAnnotation(RdfProperty.class);
    }

    protected String getMemberPath(){
        RdfProperty rdfPropertyInfo = getRdfProperty();
        RdfId rdfIdInfo = memberWrapper.getAnnotation(RdfId.class);

        if(rdfPropertyInfo!=null && !rdfPropertyInfo.path().isEmpty())
            return rdfPropertyInfo.path();
        else if(rdfIdInfo!=null && !rdfIdInfo.path().isEmpty())
            return rdfIdInfo.path();

        return "";
    }
}
