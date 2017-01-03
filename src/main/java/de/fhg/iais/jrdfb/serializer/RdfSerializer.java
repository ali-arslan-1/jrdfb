package de.fhg.iais.jrdfb.serializer;

import de.fhg.iais.jrdfb.annotation.*;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import de.fhg.iais.jrdfb.util.JenaUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VOID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfSerializer<T> {

    private Class<T> tClass;

    public RdfSerializer(Class<T> tClass){
        this.tClass = tClass;
    }
    public String serialize(T obj) throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, NoSuchFieldException {
        assert (obj != null);

        Model model = ModelFactory.createDefaultModel();

        Resource resource;
        Resource metaData;
        Object id = null;
        String baseURI = "";
        for(Field field: tClass.getDeclaredFields()) {

            field.setAccessible(true);
            if (field.isAnnotationPresent(RdfId.class)) {
                id = field.get(obj);
                baseURI = field.getAnnotation(RdfId.class).baseURI();
                break;
            }
        }
        id = (id == null || id.toString().isEmpty()) ? UUID.randomUUID() : id;

        resource = model.createResource(baseURI + id.toString());
        metaData = model.createResource();


        if(tClass.isAnnotationPresent(RdfType.class))
            resource.addProperty(RDF.type, tClass.getAnnotation(RdfType.class).value());


        for(Field field: tClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(RdfProperty.class)) {

                Object value;
                RdfProperty rdfPropertyInfo = field.getAnnotation(RdfProperty.class);
                Property jenaProperty = model.createProperty(rdfPropertyInfo.property());
                if (rdfPropertyInfo.path().isEmpty()) {
                    value = field.get(obj);
                }
                else
                {
                    value = ReflectUtils.getNestedField(obj, field ,rdfPropertyInfo.path());
                }

                if(value != null && !value.toString().isEmpty()){

                    metaData.addProperty(jenaProperty, value.getClass().getName());

                    if(field.isAnnotationPresent(RdfTypedLiteral.class)){
                        resource.addProperty(jenaProperty,
                                model.createTypedLiteral(value.toString(),
                                field.getAnnotation(RdfTypedLiteral.class).value()));
                    }
                    else if(field.isAnnotationPresent(RdfBag.class))
                    {
                        if(value instanceof Map){
                            Map map = (Map) value;
                            Bag propertiesBag = model.createBag();

                            Iterator it = map.entrySet().iterator();

                            while(it.hasNext()){
                                Map.Entry pair = (Map.Entry)it.next();
                                propertiesBag.add(model.createResource()
                                        .addProperty(DCTerms.identifier, pair.getKey().toString())
                                        .addProperty(RDF.value, pair.getValue().toString()));
                            }

                            resource.addProperty(jenaProperty, propertiesBag);

                        }
                        else
                        {
                            throw new UnsupportedOperationException
                                    ("At the moment, only java.util.Map is supported");
                        }
                    }
                    else
                    {
                        resource.addProperty(jenaProperty, value.toString());
                    }
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

    public T deserialize(String data) throws Exception {
        assert (data != null);

        T obj = tClass.newInstance();

        String RDFType;
        if(tClass.isAnnotationPresent(RdfType.class))
            RDFType = tClass.getAnnotation(RdfType.class).value();
        else
            throw new Exception("RdfType for the Class "+tClass.getName()+" is not provided.");


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
                Property jenaProperty = model.createProperty(rdfPropertyInfo.property());
                value = resource.getProperty(jenaProperty);
                if (rdfPropertyInfo.path().isEmpty()) {

                    if (value == null) continue;

                    String propertyClassName = metadata.getProperty(jenaProperty).getObject()
                            .toString();
                    //System.out.println("serialized type: "+ propertyClassName);

                    if (Map.class.isAssignableFrom(Class.forName(propertyClassName))){
                        propertyVal = JenaUtils.bagToMap(value.getBag());
                    }else if(field.isAnnotationPresent(RdfTypedLiteral.class)){
                        propertyVal = ReflectUtils.stringToObject(propertyClassName,
                                value.getLiteral().getString());
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
