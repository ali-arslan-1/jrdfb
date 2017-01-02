package de.fhg.iml.jrdfb.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfTypedLiteral {
    String value();
}
