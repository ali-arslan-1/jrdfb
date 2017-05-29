package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfBag;
import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MapMarshaller extends BasePropMarshaller {

    public MapMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        super(field, rdfMarshaller);
    }

    public MapMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        super(method, rdfMarshaller);
    }

    @Override
    public RDFNode resolveMember(@NotNull Object object, Model model) throws ReflectiveOperationException {
        Object value = memberWrapper.extractMemberValue(object);
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
