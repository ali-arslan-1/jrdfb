package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.serializer.MemberWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public abstract class AbstractMemberMarshaller implements MemberMarshaller {
    protected MemberWrapper memberWrapper;
    RdfMarshaller rdfMarshaller;

    AbstractMemberMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        this.memberWrapper = new MemberWrapper(field);
        this.rdfMarshaller = rdfMarshaller;
    }

    AbstractMemberMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        this.memberWrapper = new MemberWrapper(method);
        this.rdfMarshaller = rdfMarshaller;
    }

    /**
     * @param object whose member class name to be resolved
     * @return java class name of member
     * @throws ReflectiveOperationException
     */
    @Nullable String resolveMemberClassName(@NotNull Object object)
            throws ReflectiveOperationException {
        Object extractedValue = memberWrapper.extractMemberValue(object);
        if(extractedValue == null) return null;
        return extractedValue.getClass().getName();
    }

}
