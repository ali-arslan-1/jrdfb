package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.VOID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if(getRdfProperty().path().isEmpty()){
            return field.get(object);
        }else{
            return ReflectUtils.getNestedField(object, field , getRdfProperty().path());
        }
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource) throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(value==null)return null;
        if(getRdfProperty().path().isEmpty()){
            return ReflectUtils.stringToObject(resolveFieldClassName(resource),
                    value.getLiteral().getString());
        }else{
            return ReflectUtils.initNestedField(Class.forName(
                    field.getDeclaringClass().getName()).newInstance(),
                    field,
                    getRdfProperty().path(),
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
    public String resolveFieldClassName(@NotNull Resource resource){
        Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();

        return metadata.getProperty(getJenaProperty()).getObject().toString();
    }

    protected Property getJenaProperty(){
        return model.createProperty(getRdfProperty().value());
    }

    protected RdfProperty getRdfProperty(){
        RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
        return rdfPropertyInfo;
    }
}
