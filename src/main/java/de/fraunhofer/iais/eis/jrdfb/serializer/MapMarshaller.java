package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfBag;
import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MapMarshaller extends BasePropMarshaller {

    public MapMarshaller(Field field, RdfSerializer rdfSerializer) {
        super(field, rdfSerializer);
    }

    public MapMarshaller(Method method, RdfSerializer rdfSerializer) {
        super(method, rdfSerializer);
    }

    @Override
    public @Nullable RDFNode resolveMember(@NotNull Object object) throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        if(value == null) return null;

        RDFNode rdfNode = null;
        if(memberWrapper.isAnnotationPresent(RdfBag.class)){
            Map map = (Map) value;
            Bag propertiesBag = model.createBag();

            for (Object o : map.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                propertiesBag.add(model.createResource()
                        .addProperty(DCTerms.identifier, pair.getKey().toString())
                        .addProperty(RDF.value, pair.getValue().toString()));
            }

            rdfNode = propertiesBag;
        }

        return rdfNode;
    }
}
