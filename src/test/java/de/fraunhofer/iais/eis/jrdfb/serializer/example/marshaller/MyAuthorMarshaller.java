package de.fraunhofer.iais.eis.jrdfb.serializer.example.marshaller;

import de.fraunhofer.iais.eis.jrdfb.serializer.example.Author;
import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.PropertyMarshaller;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MyAuthorMarshaller implements PropertyMarshaller{
    @Override
    public RDFNode resolveMember(@NotNull Object object, Model model)
            throws ReflectiveOperationException {
        if(object instanceof Author) {
            Resource author = model.createResource();
            author.addProperty(VCARD.FN, ((Author) object).getName());
            author.addProperty(VCARD.CATEGORIES, "Computer Science");
            return author;
        }
        return null;
    }
}
