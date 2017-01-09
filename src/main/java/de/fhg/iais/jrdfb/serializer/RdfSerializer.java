package de.fhg.iais.jrdfb.serializer;

import de.fhg.iais.jrdfb.annotation.RdfId;
import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.annotation.RdfType;
import de.fhg.iais.jrdfb.annotation.RdfTypedLiteral;
import de.fhg.iais.jrdfb.resolver.Resolver;
import de.fhg.iais.jrdfb.resolver.ResolverFactory;
import de.fhg.iais.jrdfb.resolver.ResolverFactoryImpl;
import de.fhg.iais.jrdfb.util.JenaUtils;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VOID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfSerializer<T> {

    private Class<T> tClass;
    private ResolverFactory resolverFactory = new ResolverFactoryImpl();

    public RdfSerializer(Class<T> tClass){
        this.tClass = tClass;
    }
    public String serialize(T obj) throws ReflectiveOperationException {
        assert (obj != null);

        Model model = ModelFactory.createDefaultModel();

        Resource resource;
        Resource metaData;
        Object id = null;
        String uriTemplate = "";
        for(Field field: tClass.getDeclaredFields()) {

            field.setAccessible(true);
            if (field.isAnnotationPresent(RdfId.class)) {
                id = field.get(obj);
                uriTemplate = field.getAnnotation(RdfId.class).uriTemplate();
                break;
            }
        }
        id = (id == null || id.toString().isEmpty()) ? UUID.randomUUID() : id;

        if(uriTemplate.contains("{RfdId}")){
            id = uriTemplate.replace("{RfdId}", id.toString());
        }

        resource = model.createResource(id.toString());
        metaData = model.createResource();


        if(tClass.isAnnotationPresent(RdfType.class))
            resource.addProperty(RDF.type, tClass.getAnnotation(RdfType.class).value());


        for(Field field: tClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(RdfProperty.class)) {
                RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
                Property jenaProperty = model.createProperty(rdfPropertyInfo.value());
                Resolver resolver = resolverFactory.createResolver(field, model);
                RDFNode resolvedNode = resolver.resolveField(obj);
                if(resolvedNode != null) {
                    resource.addProperty(jenaProperty, resolvedNode);
                    metaData.addProperty(jenaProperty, resolver.resolveFieldClassName(obj));
                }

            }
        }

        resource.addProperty(VOID.dataDump, metaData);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        model.write(out, "TURTLE");

        String result = null;
        try {
            result = out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public T deserialize(String data) throws ReflectiveOperationException {
        assert (data != null);

        T obj = tClass.newInstance();

        String RDFType;
        if(tClass.isAnnotationPresent(RdfType.class))
            RDFType = tClass.getAnnotation(RdfType.class).value();
        else
            throw new ReflectiveOperationException("RdfType for the Class "+tClass.getName()+" is not provided.");


        Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(data.getBytes()), null, "TURTLE");

        ResIterator iter = model.listResourcesWithProperty(RDF.type, RDFType);
        Resource resource = iter.hasNext()? iter.nextResource(): null;

        if(resource==null) return null;

        for(Field field: tClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(RdfProperty.class)) {

                Statement value = null;
                Object propertyVal = null;
                Resource metadata = (Resource)resource.getProperty(VOID.dataDump).getObject();

                RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
                Property jenaProperty = model.createProperty(rdfPropertyInfo.value());
                value = resource.getProperty(jenaProperty);
                if (rdfPropertyInfo.path().isEmpty()) {

                    if (value == null) continue;

                    String propertyClassName = metadata.getProperty(jenaProperty).getObject()
                            .toString();
                    //System.out.println("serialized type: "+ metaData);

                    if (Map.class.isAssignableFrom(Class.forName(propertyClassName))){
                        propertyVal = JenaUtils.bagToMap(value.getBag());
                    }else if(field.isAnnotationPresent(RdfTypedLiteral.class)){
                        propertyVal = resolverFactory.createResolver(field, model).resolveProperty(resource);
                    }
                    else
                    {
                        propertyVal = ReflectUtils.stringToObject(propertyClassName,
                                value.getObject().toString());
                    }
                }
                else
                {
                    propertyVal = ReflectUtils.initNestedField(obj, field ,rdfPropertyInfo.path(),
                            value.getObject().toString());
                }

                field.set(obj, propertyVal);
            }
        }

        return obj;
    }

}
