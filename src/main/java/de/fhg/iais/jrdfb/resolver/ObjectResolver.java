package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfProperty;
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
    public RDFNode resolveField(Object object) throws IllegalAccessException {
        return null;
    }

    @Override
    public Object resolveProperty(Resource resource) throws ClassNotFoundException {
        return null;
    }

    protected String getFieldClassName(Resource resource){
        Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();

        return metadata.getProperty(getRdfProperty()).getObject().toString();
    }

    protected Property getRdfProperty(){
        RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
        Property jenaProperty = model.createProperty(rdfPropertyInfo.value());
        return jenaProperty;
    }
}
