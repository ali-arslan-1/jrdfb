package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.Model;

import java.lang.reflect.AccessibleObject;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface ResolverFactory {
    ObjectResolver createResolver(AccessibleObject accessibleObject, Model model);
}
