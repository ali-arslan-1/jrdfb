package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfBag;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MapResolver extends ObjectResolver {

    public MapResolver(Field field, Model model) {
        super(field, model);
    }

    @Override
    public @Nullable RDFNode resolveMember(@NotNull Object object) throws ReflectiveOperationException {
        Object value = extractFieldValue(object);
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

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource) throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(memberWrapper.isAnnotationPresent(RdfBag.class)){
            SortedMap<String, String> map = null;

            NodeIterator bagItr = value.getBag().iterator();

            if (bagItr.hasNext()) {
                map = new TreeMap<>();
                while (bagItr.hasNext()) {
                    Resource item = ((Resource) bagItr.next());
                    map.put(item.getProperty(DCTerms.identifier).getObject().toString(),
                            item.getProperty(RDF.value).getObject().toString());
                }

            }
            return map;
        }
        return super.resolveProperty(resource);
    }
}
