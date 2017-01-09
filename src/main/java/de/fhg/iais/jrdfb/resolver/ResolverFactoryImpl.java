package de.fhg.iais.jrdfb.resolver;

import de.fhg.iais.jrdfb.annotation.RdfTypedLiteral;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ResolverFactoryImpl implements ResolverFactory {
    @Override
    public Resolver createResolver(Field field) {

        Resolver resolver = new ObjectResolver(field);

        if(field.isAnnotationPresent(RdfTypedLiteral.class)){
            resolver = new LiteralResolver(field);
        }

        return resolver;
    }

}
