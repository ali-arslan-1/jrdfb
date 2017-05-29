package de.fraunhofer.iais.eis.jrdfb.annotation;

import de.fraunhofer.iais.eis.jrdfb.serializer.marshaller.MemberMarshaller;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 *
 * This annoation is used to provide your custom implementation for marshalling
 * a class element.
 */
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RdfMarshaller {
    /**
     * It accepts your custom implementation class as parameter which implements
     * PropertyMarshaller.
     * The field or method with this annotation will use the provided
     * PropertyMarshaller implementation to marshall the field contents.
     */
    Class<? extends MemberMarshaller> value();
}
