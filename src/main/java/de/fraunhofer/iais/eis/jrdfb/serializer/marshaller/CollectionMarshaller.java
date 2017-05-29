package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionMarshaller extends AbstractMemberMarshaller {

    public CollectionMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        super(field, rdfMarshaller);
    }

    public CollectionMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        super(method, rdfMarshaller);
    }

    @Override
    public RDFNode marshalMember(@NotNull Object object, Model model) throws ReflectiveOperationException {
        Object value = memberWrapper.extractMemberValue(object);
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
                else if(memberWrapper.getGenericTypeArgument() instanceof Class && RDFNode.class
                        .isAssignableFrom((Class<?>) memberWrapper.getGenericTypeArgument())){
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
}
