package de.fhg.iais.jrdfb.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */

@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfId {
    String baseURI() default "";
}
