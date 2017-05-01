package de.fraunhofer.iais.eis.jrdfb.serializer;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MarshallerFactoryImpl implements MarshallerFactory {

    @Override
    public BasePropMarshaller createMarshaller(AccessibleObject accessibleObject,
                                               RdfMarshaller rdfMarshaller) {
        BasePropMarshaller marshaller = null;
        if(accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            marshaller = new LiteralMarshaller(field, rdfMarshaller);

            if (Collection.class.isAssignableFrom(field.getType())) {
                marshaller = new CollectionMarshaller(field, rdfMarshaller);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                marshaller = new MapMarshaller(field, rdfMarshaller);
            } else if (field.getType() instanceof Class
                    && (field.getType()).isEnum() ){
                marshaller = new EnumMarshaller(field, rdfMarshaller);
            }
        }else if(accessibleObject instanceof Method){
            Method method = (Method) accessibleObject;
            marshaller = new LiteralMarshaller(method, rdfMarshaller);

            if(Collection.class.isAssignableFrom(method.getReturnType())){
                marshaller = new CollectionMarshaller(method, rdfMarshaller);
            }else if(Map.class.isAssignableFrom(method.getReturnType())){
                marshaller = new MapMarshaller(method, rdfMarshaller);
            }else if (method.getReturnType() instanceof Class
                    && (method.getReturnType()).isEnum()){
                marshaller = new EnumMarshaller(method, rdfMarshaller);
            }
        }
        return marshaller;
    }

    @Override
    public BasePropUnmarshaller createUnmarshaller(AccessibleObject accessibleObject, 
                                                   RdfUnmarshaller rdfMarshaller) {
        BasePropUnmarshaller unmarshaller = null;
        if(accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            unmarshaller = new LiteralUnmarshaller(field, rdfMarshaller);

            if (Collection.class.isAssignableFrom(field.getType())) {
                unmarshaller = new CollectionUnmarshaller(field, rdfMarshaller);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                unmarshaller = new MapUnmarshaller(field, rdfMarshaller);
            } else if (field.getType() instanceof Class
                    && (field.getType()).isEnum() ){
                unmarshaller = new EnumUnmarshaller(field, rdfMarshaller);
            }
        }else if(accessibleObject instanceof Method){
            Method method = (Method) accessibleObject;
            unmarshaller = new LiteralUnmarshaller(method, rdfMarshaller);

            if(Collection.class.isAssignableFrom(method.getReturnType())){
                unmarshaller = new CollectionUnmarshaller(method, rdfMarshaller);
            }else if(Map.class.isAssignableFrom(method.getReturnType())){
                unmarshaller = new MapUnmarshaller(method, rdfMarshaller);
            }else if (method.getReturnType() instanceof Class
                    && (method.getReturnType()).isEnum()){
                unmarshaller = new EnumUnmarshaller(method, rdfMarshaller);
            }
        }
        return unmarshaller;
    }


}
