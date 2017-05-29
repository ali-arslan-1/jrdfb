package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.JrdfbException;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.serializer.Factory;
import de.fraunhofer.iais.eis.jrdfb.serializer.FactoryImpl;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.IAIS;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class RdfMarshaller {

    Class[] tClasses;
    private Factory factory;
    Model model;

    private boolean isRoot;

    public RdfMarshaller(Class... tClasses){
        this.tClasses = tClasses;
        factory = new FactoryImpl();
    }
    public String marshal(@NotNull Object obj) throws JrdfbException {
        model = ModelFactory.createDefaultModel();
        isRoot = true;

        try {
            this.createResource(obj);
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

    Resource createResource(@NotNull Object obj) throws
            ReflectiveOperationException{
        Resource resource;
        Resource metaData;
        Object id = null;
        String uriTemplate = "";

        Set allAccessibleObjects = new HashSet();
        for(Class<?> clazz: tClasses) {
            if(clazz.isAssignableFrom(obj.getClass())){
                allAccessibleObjects.addAll(ReflectUtils.getFieldsUpTo(clazz, Object
                        .class));
                allAccessibleObjects.addAll(ReflectUtils.getAllMethodsInHierarchy
                        (clazz, Object.class));
            }
        }

        Collection<AccessibleObject> allMembers =
                (Collection<AccessibleObject>)(Collection<?>) allAccessibleObjects;

        for(AccessibleObject member: allMembers) {

            member.setAccessible(true);
            if (member.isAnnotationPresent(RdfId.class)) {
                PropertyMarshaller marshaller = factory.createMarshaller(member, this);
                RDFNode resolvedNode = marshaller.resolveMember(obj, model);
                if(resolvedNode == null) break;
                if(resolvedNode.isLiteral())
                    id =   ((Literal)resolvedNode).getString();
                else
                    id = resolvedNode.toString();

                uriTemplate = member.getAnnotation(RdfId.class).uriTemplate();
                break;
            }
        }


        if(id !=null && uriTemplate.contains("{RdfId}")){
            id = uriTemplate.replace("{RdfId}", id.toString());
        }

        resource = id == null? model.createResource(): model.createResource(id.toString());
        metaData = model.createResource();

        RdfType rdfTypeAnnotation = AnnotationUtils.findAnnotation(obj.getClass(), RdfType.class);
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
            de.fraunhofer.iais.eis.jrdfb.annotation.RdfMarshaller
                    customMarshallerInfo;
            if(member instanceof Method){
                rdfPropertyInfo = AnnotationUtils.findAnnotation((Method)
                        member, RdfProperty.class);
                customMarshallerInfo = AnnotationUtils.findAnnotation((Method)
                        member, de.fraunhofer.iais.eis.jrdfb.annotation
                        .RdfMarshaller.class);
            }
            else{
                rdfPropertyInfo = AnnotationUtils.findAnnotation(member,
                        RdfProperty.class);

                customMarshallerInfo = AnnotationUtils.findAnnotation(member,
                        de.fraunhofer.iais.eis.jrdfb.annotation
                                                        .RdfMarshaller.class);
            }

            if (rdfPropertyInfo !=null ){
                Property jenaProperty = model.createProperty(rdfPropertyInfo.value());

                if(customMarshallerInfo != null){
                    Object memberValue;
                    if(member instanceof Method)
                        memberValue = ((Method) member).invoke(obj);
                    else
                        memberValue = ((Field) member).get(obj);
                    PropertyMarshaller customMarshaller =
                            (PropertyMarshaller)ReflectUtils.initClassInstance
                            (customMarshallerInfo.value());
                    resource.addProperty(jenaProperty, customMarshaller
                            .resolveMember(memberValue, model));
                    metaData.addProperty(jenaProperty,
                            memberValue.getClass().getName());
                    continue;
                }

                BasePropMarshaller marshaller = factory.createMarshaller(member,this);
                boolean resolved = false;

                Class tClass = ReflectUtils.getIfAssignableFromAny(tClasses,
                        marshaller.resolveMemberClassName(obj));
                if(tClass != null){
                    Object resolvedObj = marshaller.memberWrapper.getMemberValue(obj);
                    if(resolvedObj!= null)
                        resource.addProperty(jenaProperty,
                                this.createResource(resolvedObj));

                    resolved = true;
                }


                if(!resolved){
                    RDFNode resolvedNode = marshaller.resolveMember(obj, model);
                    if(resolvedNode != null) {
                        resource.addProperty(jenaProperty, resolvedNode);
                    }
                }
                String memberClassName = marshaller.resolveMemberClassName(obj);
                if(memberClassName != null)
                    metaData.addProperty(jenaProperty, memberClassName);
            }
        }

        resource.addProperty(model.createProperty(IAIS.CLASS_MAPPING), metaData);

        return resource;
    }
}
