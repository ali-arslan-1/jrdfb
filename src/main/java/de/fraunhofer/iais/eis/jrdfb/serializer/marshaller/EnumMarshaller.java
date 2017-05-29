package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfUri;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class EnumMarshaller extends AbstractMemberMarshaller {

    public EnumMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        super(field, rdfMarshaller);
    }

    public EnumMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        super(method, rdfMarshaller);
    }

    @Override
    public RDFNode marshalMember(@NotNull Object object, Model model)
            throws ReflectiveOperationException {
        Object value = memberWrapper.extractMemberValue(object);
        if(value == null) return null;

        RDFNode rdfNode;
        RdfUri rdfUri = memberWrapper.getType().getField(((Enum)value).name())
                .getAnnotation(RdfUri.class);
        if(rdfUri != null){
            rdfNode =  model.createProperty(rdfUri.value());
        }else{
            rdfNode = model.createLiteral(value.toString());
        }

        return rdfNode;
    }
}
