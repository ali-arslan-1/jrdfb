package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface Resolver {
    @NotNull RDFNode resolveField(@NotNull Object object) throws ReflectiveOperationException;
    @NotNull Object resolveProperty(@NotNull Resource resource) throws ReflectiveOperationException;
    @NotNull String resolveFieldClassName(@NotNull Object object) throws ReflectiveOperationException;
    @NotNull String resolveFieldClassName(@NotNull Resource resource);
}
