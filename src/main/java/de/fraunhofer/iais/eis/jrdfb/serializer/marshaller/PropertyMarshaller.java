package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public interface PropertyMarshaller {
    /**
     * @param object the java object to be serialized into rdf
     * @param model jena model used to build up the entire RDF graph
     * @return RDFNode object
     * @throws ReflectiveOperationException
     */
    RDFNode resolveMember(@NotNull Object object, Model model)
            throws ReflectiveOperationException;

}
