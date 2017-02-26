package de.fraunhofer.iais.eis.jrdfb.serializer;

import java.lang.reflect.AccessibleObject;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface ResolverFactory {
    ObjectResolver createResolver(AccessibleObject accessibleObject,
                                  RdfSerializer rdfSerializer);
}
