package de.fraunhofer.iais.eis.jrdfb.serializer;

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
    public ObjectResolver createResolver(AccessibleObject accessibleObject,
                                         RdfSerializer rdfSerializer) {
        ObjectResolver resolver = null;
        if(accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            resolver = new LiteralResolver(field, rdfSerializer);

            if (Collection.class.isAssignableFrom(field.getType())) {
                resolver = new CollectionResolver(field, rdfSerializer);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                resolver = new MapResolver(field, rdfSerializer);
            } else if (field.getType() instanceof Class
                    && (field.getType()).isEnum() ){
                resolver = new EnumResolver(field, rdfSerializer);
            }
        }else if(accessibleObject instanceof Method){
            Method method = (Method) accessibleObject;
            resolver = new LiteralResolver(method, rdfSerializer);

            if(Collection.class.isAssignableFrom(method.getReturnType())){
                resolver = new CollectionResolver(method, rdfSerializer);
            }else if(Map.class.isAssignableFrom(method.getReturnType())){
                resolver = new MapResolver(method, rdfSerializer);
            }else if (method.getReturnType() instanceof Class
                    && (method.getReturnType()).isEnum()){
                resolver = new EnumResolver(method, rdfSerializer);
            }
        }
        return resolver;
    }


}
