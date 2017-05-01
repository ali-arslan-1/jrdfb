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
                                               RdfSerializer rdfSerializer) {
        BasePropMarshaller marshaller = null;
        if(accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            marshaller = new LiteralMarshaller(field, rdfSerializer);

            if (Collection.class.isAssignableFrom(field.getType())) {
                marshaller = new CollectionMarshaller(field, rdfSerializer);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                marshaller = new MapMarshaller(field, rdfSerializer);
            } else if (field.getType() instanceof Class
                    && (field.getType()).isEnum() ){
                marshaller = new EnumMarshaller(field, rdfSerializer);
            }
        }else if(accessibleObject instanceof Method){
            Method method = (Method) accessibleObject;
            marshaller = new LiteralMarshaller(method, rdfSerializer);

            if(Collection.class.isAssignableFrom(method.getReturnType())){
                marshaller = new CollectionMarshaller(method, rdfSerializer);
            }else if(Map.class.isAssignableFrom(method.getReturnType())){
                marshaller = new MapMarshaller(method, rdfSerializer);
            }else if (method.getReturnType() instanceof Class
                    && (method.getReturnType()).isEnum()){
                marshaller = new EnumMarshaller(method, rdfSerializer);
            }
        }
        return marshaller;
    }

    @Override
    public BasePropUnmarshaller createUnmarshaller(AccessibleObject accessibleObject, 
                                                   RdfSerializer rdfSerializer) {
        BasePropUnmarshaller unmarshaller = null;
        if(accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            unmarshaller = new LiteralUnmarshaller(field, rdfSerializer);

            if (Collection.class.isAssignableFrom(field.getType())) {
                unmarshaller = new CollectionUnmarshaller(field, rdfSerializer);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                unmarshaller = new MapUnmarshaller(field, rdfSerializer);
            } else if (field.getType() instanceof Class
                    && (field.getType()).isEnum() ){
                unmarshaller = new EnumUnmarshaller(field, rdfSerializer);
            }
        }else if(accessibleObject instanceof Method){
            Method method = (Method) accessibleObject;
            unmarshaller = new LiteralUnmarshaller(method, rdfSerializer);

            if(Collection.class.isAssignableFrom(method.getReturnType())){
                unmarshaller = new CollectionUnmarshaller(method, rdfSerializer);
            }else if(Map.class.isAssignableFrom(method.getReturnType())){
                unmarshaller = new MapUnmarshaller(method, rdfSerializer);
            }else if (method.getReturnType() instanceof Class
                    && (method.getReturnType()).isEnum()){
                unmarshaller = new EnumUnmarshaller(method, rdfSerializer);
            }
        }
        return unmarshaller;
    }


}
