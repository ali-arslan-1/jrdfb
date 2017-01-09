package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfTypedLiteral;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.Property;
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
    public RDFNode resolveField(Object object) throws IllegalAccessException {
        Object value = field.get(object);

        return model.createTypedLiteral(value.toString(),
                field.getAnnotation(RdfTypedLiteral.class).value());
    }

    @Override
    public Object resolveProperty(Resource resource) throws ClassNotFoundException {
        Statement value = resource.getProperty(getRdfProperty());
        return ReflectUtils.stringToObject(getFieldClassName(resource),
                value.getLiteral().getString());
    }
}
