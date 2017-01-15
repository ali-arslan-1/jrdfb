package de.fhg.iais.jrdfb.serializer;

import de.fhg.iais.jrdfb.annotation.RdfId;
import de.fhg.iais.jrdfb.annotation.RdfProperty;
import de.fhg.iais.jrdfb.annotation.RdfType;
import de.fhg.iais.jrdfb.resolver.Resolver;
import de.fhg.iais.jrdfb.resolver.ResolverFactory;
import de.fhg.iais.jrdfb.resolver.ResolverFactoryImpl;
import de.fhg.iais.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VOID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfSerializer {

    private Class[] tClasses;
    private ResolverFactory resolverFactory = new ResolverFactoryImpl();

    public RdfSerializer(Class... tClasses){
        //this.rootClass = rootClass;
        this.tClasses = tClasses;
    }
    public String serialize(Object obj) throws ReflectiveOperationException {
        assert (obj != null);
        Class rootClass = tClasses[0];
        for(Class clazz: tClasses){
            if(clazz.isInstance(obj))
                rootClass = clazz;
        }
        Model model = ModelFactory.createDefaultModel();

        Resource resource;
        Resource metaData;
        Object id = null;
        String uriTemplate = "";
        final Iterable<Field> allFields = ReflectUtils.getFieldsUpTo(rootClass, Object.class);
        for(Field field: allFields) {
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


        if(rootClass.isAnnotationPresent(RdfType.class))
            resource.addProperty(RDF.type, ((RdfType) rootClass.getAnnotation(RdfType.class)).value());


        for(Field field: allFields) {
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

    public Object deserialize(String data) throws ReflectiveOperationException {
        assert (data != null);

        Class rootClass = tClasses[0]; //todo

        Constructor cons = rootClass.getDeclaredConstructor();
        cons.setAccessible(true);
        Object obj = cons.newInstance();

        String RDFType;
        if(rootClass.isAnnotationPresent(RdfType.class))
            RDFType = ((RdfType) rootClass.getAnnotation(RdfType.class)).value();
        else
            throw new ReflectiveOperationException("RdfType for the Class "+ rootClass.getName()+" is not provided.");


        Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(data.getBytes()), null, "TURTLE");

        ResIterator iter = model.listResourcesWithProperty(RDF.type, RDFType);
        Resource resource = iter.hasNext()? iter.nextResource(): null;

        if(resource==null) return null;

        final Iterable<Field> allFields = ReflectUtils.getFieldsUpTo(rootClass, Object.class);
        for(Field field: allFields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(RdfProperty.class)) {

                Object propertyVal = resolverFactory.createResolver(field, model).resolveProperty(resource);

                field.set(obj, propertyVal);
            }
        }

        return obj;
    }

}
