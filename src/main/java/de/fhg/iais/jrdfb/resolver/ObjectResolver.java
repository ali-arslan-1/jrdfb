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
    protected Field field;
    protected Model model;

    public ObjectResolver(Field field, Model model) {
        this.field = field;
        this.model = model;
    }

    protected Object extractFieldValue(Object object) throws ReflectiveOperationException {
        if(getFieldPath().isEmpty()){
            return field.get(object);
        }else{
            return ReflectUtils.getNestedField(object, field , getFieldPath());
        }
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource) throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(value==null)return null;
        if(getFieldPath().isEmpty()){
            return ReflectUtils.stringToObject(resolveFieldClassName(resource),
                    value.getLiteral().getString());
        }else{
            Constructor cons = Class.forName(
                    field.getDeclaringClass().getName()).getDeclaredConstructor();
            cons.setAccessible(true);
            return ReflectUtils.initNestedField(cons.newInstance(),
                    field,
                    getFieldPath(),
                    value.getObject().toString());
        }
    }

    @NotNull
    @Override
    public String resolveFieldClassName(@NotNull Object object) throws ReflectiveOperationException {
        return extractFieldValue(object).getClass().getName();
    }

    @Override
    @Nullable
    public String resolveFieldClassName(@NotNull Resource resource)
            throws ReflectiveOperationException{
        Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();
        Statement metaProperty = metadata.getProperty(getJenaProperty());
        if(metaProperty==null)
            throw new NoSuchFieldException("Metadata for field '"+field.getName()+"' Not provided" +
                    " in RDF Resource: "+resource.getURI());
        return metaProperty.getObject().toString();
    }

    protected Property getJenaProperty(){
        return model.createProperty(getRdfProperty().value());
    }

    protected RdfProperty getRdfProperty(){
        RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
        return rdfPropertyInfo;
    }

    protected String getFieldPath(){
        RdfProperty rdfPropertyInfo = getRdfProperty();
        RdfId rdfIdInfo = field.getAnnotation(RdfId.class);

        if(rdfPropertyInfo!=null && !rdfPropertyInfo.path().isEmpty())
            return rdfPropertyInfo.path();
        else if(rdfIdInfo!=null && !rdfIdInfo.path().isEmpty())
            return rdfIdInfo.path();

        return "";
    }
}
