package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.Model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ResolverFactoryImpl implements ResolverFactory {

    @Override
    public Resolver createResolver(Field field, Model model) {
        Resolver resolver = new LiteralResolver(field, model);

        if(Collection.class.isAssignableFrom(field.getType())){
            resolver = new CollectionResolver(field, model);
        }else if(Map.class.isAssignableFrom(field.getType())){
            resolver = new MapResolver(field, model);
        }

        return resolver;
    }

}
