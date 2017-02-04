package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfId;
import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.VOID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public abstract class ObjectResolver implements Resolver {
    protected MemberWrapper memberWrapper;
    protected Model model;

    public ObjectResolver(Field field, Model model) {
        this.memberWrapper = new MemberWrapper(field);
        this.model = model;
    }

    protected Object extractFieldValue(Object object) throws ReflectiveOperationException {
        if(getFieldPath().isEmpty()){
            return memberWrapper.getValue(object);
        }else{
            return memberWrapper.getNestedObject(object, getFieldPath());
        }
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(value==null)return null;
        if(getFieldPath().isEmpty()){
            return ReflectUtils.stringToObject(resolveMemberClassName(resource),
                    value.getLiteral().getString());
        }else{
            Constructor cons = Class.forName(
                    memberWrapper.getDeclaringClass().getName())
                    .getDeclaredConstructor();
            cons.setAccessible(true);
            return memberWrapper.initNestedObject(cons.newInstance(),
                    getFieldPath(),
                    value.getObject().toString());
        }
    }

    @NotNull
    @Override
    public String resolveMemberClassName(@NotNull Object object) throws ReflectiveOperationException {
        return extractFieldValue(object).getClass().getName();
    }

    @Override
    @Nullable
    public String resolveMemberClassName(@NotNull Resource resource)
            throws ReflectiveOperationException{
        Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();
        Statement metaProperty = metadata.getProperty(getJenaProperty());
        if(metaProperty==null)
            throw new NoSuchFieldException("Metadata for memberWrapper '"
                    + memberWrapper.getName()+"' Not provided" +
                    " in RDF Resource: "+resource.getURI());
        return metaProperty.getObject().toString();
    }

    protected Property getJenaProperty(){
        return model.createProperty(getRdfProperty().value());
    }

    protected RdfProperty getRdfProperty(){
        RdfProperty rdfPropertyInfo = memberWrapper.getAnnotation(RdfProperty.class);
        return rdfPropertyInfo;
    }

    protected String getFieldPath(){
        RdfProperty rdfPropertyInfo = getRdfProperty();
        RdfId rdfIdInfo = memberWrapper.getAnnotation(RdfId.class);

        if(rdfPropertyInfo!=null && !rdfPropertyInfo.path().isEmpty())
            return rdfPropertyInfo.path();
        else if(rdfIdInfo!=null && !rdfIdInfo.path().isEmpty())
            return rdfIdInfo.path();

        return "";
    }
}
