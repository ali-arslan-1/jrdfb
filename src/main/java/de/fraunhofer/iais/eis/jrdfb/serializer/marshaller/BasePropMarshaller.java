package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.serializer.MemberWrapper;
import org.apache.jena.rdf.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public abstract class BasePropMarshaller implements PropertyMarshaller {
    protected MemberWrapper memberWrapper;
    protected Model model;
    protected RdfMarshaller rdfMarshaller;

    public BasePropMarshaller(Field field, RdfMarshaller rdfMarshaller) {
        this.memberWrapper = new MemberWrapper(field);
        this.model = rdfMarshaller.model;
        this.rdfMarshaller = rdfMarshaller;
    }

    public BasePropMarshaller(Method method, RdfMarshaller rdfMarshaller) {
        this.memberWrapper = new MemberWrapper(method);
        this.model = rdfMarshaller.model;
        this.rdfMarshaller = rdfMarshaller;
    }

    @Nullable
    @Override
    public String resolveMemberClassName(@NotNull Object object)
            throws ReflectiveOperationException {
        Object extractedValue = memberWrapper.extractMemberValue(object);
        if(extractedValue == null) return null;
        return extractedValue.getClass().getName();
    }

}
