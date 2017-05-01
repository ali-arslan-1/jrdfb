package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfBag;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MapUnmarshaller extends BasePropUnmarshaller {

    public MapUnmarshaller(Field field, RdfUnmarshaller rdfUnmarshaller) {
        super(field, rdfUnmarshaller);
    }

    public MapUnmarshaller(Method method, RdfUnmarshaller rdfUnmarshaller) {
        super(method, rdfUnmarshaller);
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
        return null;
    }
}
