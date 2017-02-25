package de.fraunhofer.iais.eis.jrdfb.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfUri {
    String value();
}
