package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.JrdfbException;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import de.fraunhofer.iais.eis.jrdfb.util.Utils;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.IAIS;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfSerializer {

    Class[] tClasses;
    private ResolverFactory resolverFactory;
    Model model;

    private boolean isRoot;

    public RdfSerializer(Class... tClasses){
        this.tClasses = tClasses;
        resolverFactory = new ResolverFactoryImpl();
    }
    public String serialize(@NotNull Object obj) throws JrdfbException {
        model = ModelFactory.createDefaultModel();
        isRoot = true;

        Class rootClass = tClasses[0];
        for(Class clazz: tClasses){
            if(clazz.isInstance(obj))
                rootClass = clazz;
        }

        try {
            this.createResource(rootClass, obj);
        } catch (ReflectiveOperationException e) {
            throw new JrdfbException(e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        model.write(out, "TURTLE");

        String result;
        try {
            result = out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new JrdfbException(e.getMessage(), e);
        }

        return result;
    }

    Resource createResource(@NotNull Class clazz, @NotNull Object obj) throws
            ReflectiveOperationException{
        Resource resource;
        Resource metaData;
        Object id = null;
        String uriTemplate = "";
        Collection allFields = ReflectUtils.getFieldsUpTo(clazz, Object
                .class);

        Collection allMethods = ReflectUtils.getAllMethodsInHierarchy
                (clazz, Object.class);

        Collection allAccessibleObjects = new ArrayList();
        allAccessibleObjects.addAll(allFields);
        allAccessibleObjects.addAll(allMethods);

        Collection<AccessibleObject> allMembers = (Collection<AccessibleObject>)(Collection<?>)
                allAccessibleObjects;

        for(AccessibleObject member: allMembers) {

            member.setAccessible(true);
            if (member.isAnnotationPresent(RdfId.class)) {
                Resolver resolver = resolverFactory.createResolver(member, this);
                RDFNode resolvedNode = resolver.resolveMember(obj);
                assert resolvedNode != null;
                if(resolvedNode.isLiteral())
                    id =   ((Literal)resolvedNode).getString();
                else
                    id = resolvedNode.toString();

                uriTemplate = member.getAnnotation(RdfId.class).uriTemplate();
                break;
            }
        }
        id = (id == null || id.toString().isEmpty()) ? ReflectUtils.getChecksum(obj) : id;

        if(uriTemplate.contains("{RdfId}")){
            id = uriTemplate.replace("{RdfId}", id.toString());
        }

        resource = model.createResource(id.toString());
        metaData = model.createResource();

        RdfType rdfTypeAnnotation = AnnotationUtils.findAnnotation(clazz, RdfType.class);
        String rdfType;
        if(rdfTypeAnnotation != null) {
            rdfType = rdfTypeAnnotation.value();

            resource.addProperty(RDF.type, model.createProperty(rdfType));
            metaData.addProperty(model.createProperty(rdfType), obj.getClass().getName());

            if(isRoot){
                metaData.addProperty(model.createProperty(IAIS.IS_MAPPING_ROOT), "true");
                isRoot = false;
            }

        }else
            throw new NoSuchFieldException("RdfType for class '"+obj.getClass().getName()+"' " +
                    "Not provided");
        for(AccessibleObject member: allMembers) {
            member.setAccessible(true);
            RdfProperty rdfPropertyInfo;
            if(member instanceof Method)
                rdfPropertyInfo = AnnotationUtils.findAnnotation((Method)member, RdfProperty.class);
            else
                rdfPropertyInfo = AnnotationUtils.findAnnotation(member, RdfProperty.class);

            if (rdfPropertyInfo !=null ){
                Property jenaProperty = model.createProperty(rdfPropertyInfo.value());
                ObjectResolver resolver = resolverFactory.createResolver(member,this);
                boolean resolved = false;

                Class tClass = ReflectUtils.getIfAssignableFromAny(tClasses, resolver.resolveMemberClassName
                        (obj));
                if(tClass != null){
                    Object resolvedObj = resolver.getMemberValue(obj);
                    if(resolvedObj!= null)
                        resource.addProperty(jenaProperty,
                                this.createResource(tClass, resolvedObj));

                    resolved = true;
                }


                if(!resolved){
                    RDFNode resolvedNode = resolver.resolveMember(obj);
                    if(resolvedNode != null) {
                        resource.addProperty(jenaProperty, resolvedNode);
                    }
                }
                String memberClassName = resolver.resolveMemberClassName(obj);
                if(memberClassName != null)
                    metaData.addProperty(jenaProperty, memberClassName);
            }
        }

        resource.addProperty(model.createProperty(IAIS.CLASS_MAPPING), metaData);

        return resource;
    }


    public Object deserialize(@NotNull String data) throws JrdfbException {

        Class rootClass = null;
        model = ModelFactory.createDefaultModel();

        model.read(new ByteArrayInputStream(data.getBytes()), null, "TURTLE");

        Resource resource = null;
        outerloop:
        for(Class clazz: tClasses){
            RdfType rdfTypeAnnotation = AnnotationUtils.findAnnotation(clazz, RdfType.class);
            if(rdfTypeAnnotation != null){
                String rdfType  = rdfTypeAnnotation.value();
                ResIterator iter = model.listResourcesWithProperty(RDF.type,
                        model.createProperty(rdfType));
                while(iter!=null && iter.hasNext()){
                    resource = iter.nextResource();

                    Resource metadata = (Resource)resource
                                .getProperty(model.createProperty(IAIS.CLASS_MAPPING)).getObject();
                    Statement metaRdfType = metadata.getProperty(model.createProperty(rdfType));
                    if(metaRdfType==null)
                        throw new JrdfbException("Metadata for Java Class Name Not provided" +
                                " " +
                                "in RDF Resource: "+resource.getURI());
                    try {
                        if(ReflectUtils.getIfAssignableFromAny(tClasses, metaRdfType.getObject()
                                .toString()) != null
                                && (metadata
                                    .getProperty
                                            (model.createProperty(IAIS.IS_MAPPING_ROOT)) != null))
                        {
                            rootClass = clazz;
                            break outerloop;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new JrdfbException(e.getMessage(), e);
                    }
                }
            }
        }

        if(rootClass == null)
            throw new JrdfbException("No matching java class found for rdf resource: " +
                    ""+data);
        try {
            return createObject(resource);
        } catch (ReflectiveOperationException e) {
            throw new JrdfbException(e.getMessage(), e);
        }
    }

    Object createObject(@NotNull Resource resource)
            throws ReflectiveOperationException {

        Class<?> clazz = ReflectUtils.getResourceClass(resource);

        Object obj;
        Statement rdfValue = resource.getProperty(RDF.value);
        if(rdfValue != null){
            obj = ReflectUtils.stringToObject(clazz.getName(), rdfValue.getObject().toString());
        }else{
            obj = ReflectUtils.initClassInstance(clazz);
        }

        Collection allFields = ReflectUtils.getFieldsUpTo(clazz, Object
                .class);

        Collection allMethods = ReflectUtils.getAllMethodsInHierarchy
                (clazz, Object.class);

        Collection allAccessibleObjects = new ArrayList();
        allAccessibleObjects.addAll(allFields);
        allAccessibleObjects.addAll(allMethods);

        Collection<AccessibleObject> allMembers = (Collection<AccessibleObject>)(Collection<?>)
                allAccessibleObjects;


        for(AccessibleObject member: allMembers) {
            member.setAccessible(true);

            RdfProperty rdfPropertyInfo;
            RdfId rdfIdInfo;
            if(member instanceof Method){
                rdfPropertyInfo = AnnotationUtils.findAnnotation((Method)member, RdfProperty
                        .class);
                rdfIdInfo = AnnotationUtils.findAnnotation((Method)member, RdfId.class);
            }
            else{
                rdfPropertyInfo = AnnotationUtils.findAnnotation(member, RdfProperty.class);
                rdfIdInfo = AnnotationUtils.findAnnotation(member, RdfId.class);
            }

            if (rdfIdInfo !=null){
                String pluckedId = Utils.pluckIdFromUri(resource.getURI(), rdfIdInfo.uriTemplate());
                MemberWrapper wrapper = member instanceof Method?
                        (new MemberWrapper((Method) member)):
                        (new MemberWrapper((Field) member));
                wrapper.setValue(obj, ReflectUtils.stringToObject(wrapper.getType().getName(),
                                pluckedId));
            }
            if (rdfPropertyInfo != null) {

                ObjectResolver resolver = resolverFactory.createResolver(member,this);
                boolean resolved = false;
                Object propertyVal = null;
                Property jenaProperty = model.createProperty(rdfPropertyInfo.value());

                Class<?> tClass = ReflectUtils.getIfAssignableFromAny(tClasses,
                        resolver.resolveMemberClassName(resource));

                if(tClass != null){
                    propertyVal = this.createObject(
                            (Resource)resource.getProperty(jenaProperty).getObject());
                    resolved = true;
                }

                if(!resolved)
                    propertyVal = resolver.resolveProperty(resource);

                if (propertyVal != null) {
                    resolver.setMemberValue(obj, propertyVal);
                }
            }
        }

        return obj;
    }
}
