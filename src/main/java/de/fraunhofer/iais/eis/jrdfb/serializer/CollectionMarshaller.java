package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionMarshaller extends BasePropMarshaller {

    public CollectionMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        super(field, rdfMarshaller);
    }

    public CollectionMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        super(method, rdfMarshaller);
    }

    @Override
    public RDFNode resolveMember(@NotNull Object object) throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        if(value == null) return null;
        RDFNode rdfNode;

        Collection collection = (Collection)value;
        Resource resource = model.createResource();
        resource.addProperty(RDF.type, SKOS.Collection);
        Class tClass = null;

        for (Object elem : collection) {
            if(elem != null)
                tClass = ReflectUtils.getIfAssignableFromAny(rdfMarshaller.tClasses,
                        elem.getClass().getName());
            if(tClass != null){
                rdfNode = (elem == null? RDF.nil: rdfMarshaller.createResource(elem));
            }else{
                if(memberWrapper.getGenericType().equals(URL.class)){
                    rdfNode =  model.createProperty(elem.toString());
                }
                else if(getGenericType() instanceof Class && RDFNode.class
                        .isAssignableFrom((Class<?>) getGenericType())){
                    rdfNode = (RDFNode)elem;
                }
                else{
                    rdfNode = model.createLiteral(elem.toString());
                }
            }
            resource.addProperty(SKOS.member, rdfNode);
        }

        return resource;
    }

    private Type getGenericType(){
        ParameterizedType genericType = (ParameterizedType) memberWrapper
                .getGenericType();
        return genericType.getActualTypeArguments()[0];
    }
}
