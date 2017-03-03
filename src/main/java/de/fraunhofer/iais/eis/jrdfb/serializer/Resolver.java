package de.fraunhofer.iais.eis.jrdfb.serializer;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface Resolver {
    /**
     * @param object the java object to be serialized into rdf
     * @return RDFNode object
     * @throws ReflectiveOperationException
     */
    @Nullable RDFNode resolveMember(@NotNull Object object)
            throws ReflectiveOperationException;

    /**
     * @param resource the jena rdf resource object to be deserialized to java object.
     * @return deserialized java object
     * @throws ReflectiveOperationException
     */
    @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException;

    /**
     * @param object whose member class name to be resolved
     * @return java class name of member
     * @throws ReflectiveOperationException
     */
    @Nullable String resolveMemberClassName(@NotNull Object object)
            throws ReflectiveOperationException;

    /**
     * @param resource whose property class name to be resolved
     * @return java class name of property
     * @throws ReflectiveOperationException
     */
    @Nullable String resolveMemberClassName(@NotNull Resource resource)
            throws ReflectiveOperationException;
}
