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
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class CollectionUnmarshaller extends BasePropUnmarshaller {

    public CollectionUnmarshaller(Field field, RdfUnmarshaller rdfUnmarshaller) {
        super(field, rdfUnmarshaller);
    }

    public CollectionUnmarshaller(Method method, RdfUnmarshaller rdfUnmarshaller) {
        super(method, rdfUnmarshaller);
    }

    @Override
    public @Nullable Object resolveProperty(@NotNull Resource resource)
            throws ReflectiveOperationException {
        Statement value = resource.getProperty(getJenaProperty());
        if(value==null)return null;
        Resource collectionRes = (Resource)value.getObject();

        Class tClass = ReflectUtils.getIfAssignableFromAny(rdfUnmarshaller.tClasses,
                getGenericType().getTypeName());

        StmtIterator it  = collectionRes.listProperties(SKOS.member);

        Collection collection = (Collection)ReflectUtils.
                                    initClassDefaultInstance(resolveMemberClassName(resource));

        while( it.hasNext() ) {
            Statement stmt = it.nextStatement();
            if(tClass != null && getGenericType() instanceof Class){
                RDFNode elem = stmt.getObject();
                Object object = elem.equals(RDF.nil)? null
                                    : rdfUnmarshaller
                                        .createObject((Resource) elem);
                collection.add(object);
            }else{
                String stringValue;
                if(memberWrapper.getGenericType().equals(URL.class)){
                    stringValue = stmt.getObject().toString();
                }else if(getGenericType() instanceof Class && RDFNode.class
                        .isAssignableFrom((Class<?>) getGenericType())){
                    collection.add(stmt.getObject());
                    continue;
                }
                else{
                    stringValue = stmt.getLiteral().getString();
                }
                collection.add(ReflectUtils.stringToObject(getGenericType().getTypeName(),
                        stringValue));
            }

        }

        return collection;
    }

    private Type getGenericType(){
        ParameterizedType genericType = (ParameterizedType) memberWrapper
                .getGenericType();
        return genericType.getActualTypeArguments()[0];
    }
}
