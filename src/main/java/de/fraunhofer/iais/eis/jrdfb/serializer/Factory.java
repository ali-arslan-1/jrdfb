package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.AbstractMemberMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.AbstractMemberUnmarshaller;
import de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller.RdfUnmarshaller;

import java.lang.reflect.AccessibleObject;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface Factory {
    AbstractMemberMarshaller createMarshaller(AccessibleObject accessibleObject,
                                              RdfMarshaller rdfMarshaller);

    AbstractMemberUnmarshaller createUnmarshaller(AccessibleObject accessibleObject,
                                                  RdfUnmarshaller rdfUnmarshaller);
}
