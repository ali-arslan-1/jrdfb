package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface Resolver {
    RDFNode resolveField(Object object) throws ReflectiveOperationException;
    Object resolveProperty(Resource resource) throws ReflectiveOperationException;
}
