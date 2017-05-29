package de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller;

import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface MemberUnmarshaller {
    /**
     * @param resource the jena rdf resource object to be deserialized
     * to java object.
     * @return deserialized java object
     * @throws ReflectiveOperationException
     */
    @Nullable Object unmarshalMember(@NotNull Resource resource)
            throws ReflectiveOperationException;
}
