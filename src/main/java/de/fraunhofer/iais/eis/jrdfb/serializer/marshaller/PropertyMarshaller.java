package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface PropertyMarshaller {
    /**
     * @param object the java object to be serialized into rdf
     * @return RDFNode object
     * @throws ReflectiveOperationException
     */
    @Nullable RDFNode resolveMember(@NotNull Object object)
            throws ReflectiveOperationException;

    /**
     * @param object whose member class name to be resolved
     * @return java class name of member
     * @throws ReflectiveOperationException
     */
    @Nullable String resolveMemberClassName(@NotNull Object object)
            throws ReflectiveOperationException;
}
