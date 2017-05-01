package de.fraunhofer.iais.eis.jrdfb.serializer;

import java.lang.reflect.AccessibleObject;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface MarshallerFactory {
    BasePropMarshaller createMarshaller(AccessibleObject accessibleObject,
                                        RdfMarshaller rdfMarshaller);

    BasePropUnmarshaller createUnmarshaller(AccessibleObject accessibleObject,
                                        RdfUnmarshaller rdfUnmarshaller);
}
