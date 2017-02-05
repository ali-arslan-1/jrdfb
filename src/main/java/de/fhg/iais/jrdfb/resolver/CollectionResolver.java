package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionResolver extends ObjectResolver {


    public CollectionResolver(Field field, Model model) {
        super(field, model);
    }

    public CollectionResolver(Method method, Model model) {
        super(method, model);
    }

    @Override
    public RDFNode resolveMember(Object object) throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        RDFNode rdfNode = null;

        return rdfNode;
    }

}
