package de.fhg.iais.jrdfb.resolver;

import org.apache.jena.rdf.model.Model;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ResolverFactoryImpl implements ResolverFactory {

    @Override
    public Resolver createResolver(AccessibleObject accessibleObject, Model model) {
        Resolver resolver = null;
        if(accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            resolver = new LiteralResolver(field, model);

            if (Collection.class.isAssignableFrom(field.getType())) {
                resolver = new CollectionResolver(field, model);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                resolver = new MapResolver(field, model);
            }
        }else if(accessibleObject instanceof Method){
            Method method = (Method) accessibleObject;
            resolver = new LiteralResolver(method, model);

            if(Collection.class.isAssignableFrom(method.getReturnType())){
                resolver = new CollectionResolver(method, model);
            }else if(Map.class.isAssignableFrom(method.getReturnType())){
                resolver = new MapResolver(method, model);
            }
        }
        return resolver;
    }


}
