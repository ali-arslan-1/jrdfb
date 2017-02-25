package de.fraunhofer.iais.eis.jrdfb.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfTypedLiteral {
    String value();
}
