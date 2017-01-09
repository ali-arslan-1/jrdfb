package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfTypedLiteral;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class LiteralResolver extends ObjectResolver {


    public LiteralResolver(Field field) {
        super(field);
    }

    @Override
    public RDFNode resolveField(Object object) throws ReflectiveOperationException {
        Object value = field.get(object);

        return model.createTypedLiteral(extractFieldValue(object),
                field.getAnnotation(RdfTypedLiteral.class).value());
    }

    @Override
    public Object resolveProperty(Resource resource) throws ClassNotFoundException {
        Statement value = resource.getProperty(getJenaProperty());
        return ReflectUtils.stringToObject(getFieldClassName(resource),
                value.getLiteral().getString());
    }
}
