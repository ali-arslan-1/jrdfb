package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface Resolver {
    RDFNode resolveField(Object object) throws IllegalAccessException;
    Object resolveProperty(Resource resource) throws ClassNotFoundException;
}
