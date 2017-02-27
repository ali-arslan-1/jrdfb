package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionResolver extends ObjectResolver {

    public CollectionResolver(Field field, RdfSerializer rdfSerializer) {
        super(field, rdfSerializer);
    }

    public CollectionResolver(Method method, RdfSerializer rdfSerializer) {
        super(method, rdfSerializer);
    }

    @Override
    public RDFNode resolveMember(Object object) throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        if(value == null) return null;
        RDFNode rdfNode;

        Collection collection = (Collection)value;
        Resource resource = model.createResource();
        resource.addProperty(RDF.type, SKOS.Collection);

        Class tClass = ReflectUtils.getIfExists(rdfSerializer.tClasses,
                getGenericType().getName());
        for (Object elem : collection) {
            if(tClass != null){
                rdfNode = rdfSerializer.createResource(tClass, elem);
            }else{
                if(memberWrapper.getGenericType().equals(URL.class))
                    rdfNode =  model.createProperty(elem.toString());
                else
                    rdfNode = model.createLiteral(elem.toString());
            }
            resource.addProperty(SKOS.member, rdfNode);
        }

        return resource;
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(value==null)return null;
        Resource collectionRes = (Resource)value.getObject();

        Class tClass = ReflectUtils.getIfExists(rdfSerializer.tClasses,
                getGenericType().getName());

        StmtIterator it  = collectionRes.listProperties(SKOS.member);

        Collection collection = (Collection)ReflectUtils.
                                    initClassDefaultInstance(resolveMemberClassName(resource));

        while( it.hasNext() ) {
            Statement stmt = it.nextStatement();
            if(tClass != null){
                Object object = rdfSerializer.createObject(getGenericType(),
                        (Resource) stmt.getObject());
                collection.add(object);
            }else{
                String stringValue;
                if(memberWrapper.getGenericType().equals(URL.class)){
                    stringValue = stmt.getObject().toString();
                }
                else{
                    stringValue = stmt.getLiteral().getString();
                }
                collection.add(ReflectUtils.stringToObject(getGenericType().getName(),
                        stringValue));
            }

        }

        return collection;
    }

    private Class<?> getGenericType(){
        ParameterizedType genericType = (ParameterizedType) memberWrapper
                .getGenericType();
        return (Class<?>) genericType.getActualTypeArguments()[0];
    }
}
