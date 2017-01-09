package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.Model;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface ResolverFactory {
    Resolver createResolver(Field field, Model model);
}
