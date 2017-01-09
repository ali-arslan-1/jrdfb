package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VOID;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ObjectResolver implements Resolver {
    protected Field field;
    protected Model model;

    public ObjectResolver(Field field) {
        this.field = field;
        this.model = ModelFactory.createDefaultModel();
    }


    @Override
    public RDFNode resolveField(Object object) throws ReflectiveOperationException {
        return null;
    }

    @Override
    public Object resolveProperty(Resource resource) throws ReflectiveOperationException {
        return null;
    }

    protected Object extractFieldValue(Object object) throws ReflectiveOperationException {
        if(getRdfProperty().path().isEmpty()){
            return field.get(object);
        }else{
            return ReflectUtils.getNestedField(object, field , getRdfProperty().path());
        }
    }

    protected String getFieldClassName(Resource resource){
        Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();

        return metadata.getProperty(getJenaProperty()).getObject().toString();
    }

    protected Property getJenaProperty(){
        Property jenaProperty = model.createProperty(getRdfProperty().value());
        return jenaProperty;
    }

    protected RdfProperty getRdfProperty(){
        RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
        return rdfPropertyInfo;
    }
}
