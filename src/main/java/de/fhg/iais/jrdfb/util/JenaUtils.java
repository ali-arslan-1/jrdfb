package de.fhg.iais.jrdfb.util;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class JenaUtils {

    private static final Model m = ModelFactory.createDefaultModel();

    public static Property getProperty(String uri){
        return m.createProperty(uri);
    }

    public static Map bagToMap(Bag bag){

        SortedMap<String, String> map = null;

        NodeIterator bagItr = bag.iterator();

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
}
