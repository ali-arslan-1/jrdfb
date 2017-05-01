package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.BasePropMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.BasePropUnmarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.RdfUnmarshaller;

import java.lang.reflect.AccessibleObject;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface Factory {
    BasePropMarshaller createMarshaller(AccessibleObject accessibleObject,
                                        RdfMarshaller rdfMarshaller);

    BasePropUnmarshaller createUnmarshaller(AccessibleObject accessibleObject,
                                            RdfUnmarshaller rdfUnmarshaller);
}
