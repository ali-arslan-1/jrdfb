package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfTypedLiteral;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class LiteralResolver extends ObjectResolver {


    public LiteralResolver(Field field, Model model) {
        super(field, model);
    }

    public LiteralResolver(Method method, Model model) {
        super(method, model);
    }

    @Override
    public RDFNode resolveMember(@NotNull Object object) throws ReflectiveOperationException {
        Object value = extractMemberValue(object);
        if(value == null) return null;
        RDFNode rdfNode;

        if(memberWrapper.isAnnotationPresent(RdfTypedLiteral.class)){
            rdfNode =  model.createTypedLiteral(value.toString(),
                memberWrapper.getAnnotation(RdfTypedLiteral.class).value());
        }else{
            rdfNode = model.createLiteral(value.toString());
        }

        return rdfNode;
    }

}
