package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfTypedLiteral;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class LiteralResolver extends ObjectResolver {


    public LiteralResolver(Field field, Model model) {
        super(field, model);
    }

    @Override
    public RDFNode resolveField(Object object) throws ReflectiveOperationException {
        Object value = extractFieldValue(object);
        if(value == null) return null;
        RDFNode rdfNode = null;

        if(field.isAnnotationPresent(RdfTypedLiteral.class)){
            rdfNode =  model.createTypedLiteral(value.toString(),
                field.getAnnotation(RdfTypedLiteral.class).value());
        }else{
            rdfNode = model.createLiteral(value.toString());
        }

        return rdfNode;
    }

}
