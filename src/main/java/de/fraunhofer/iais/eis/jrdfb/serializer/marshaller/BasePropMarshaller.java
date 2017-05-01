package de.fraunhofer.iais.eis.jrdfb.serializer.marshaller;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.serializer.MemberWrapper;
import org.apache.commons.lang3.ClassUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
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

    protected Object extractMemberValue(Object object)
            throws ReflectiveOperationException {
        if(getMemberPath().isEmpty()){
            return getMemberValue(object);
        }else{
            return memberWrapper.getNestedObject(object, getMemberPath());
        }
    }

    public @Nullable Object getMemberValue(@NotNull Object object)
            throws ReflectiveOperationException {
        return memberWrapper.getValue(object);
    }

    public void setMemberValue(@NotNull Object object, @NotNull Object value)
            throws ReflectiveOperationException {
        if(!ClassUtils.isAssignable(value.getClass(), memberWrapper.getType())) return;
        memberWrapper.setValue(object, value);
    }

    @Nullable
    @Override
    public String resolveMemberClassName(@NotNull Object object) throws ReflectiveOperationException {
        Object extractedValue = extractMemberValue(object);
        if(extractedValue == null) return null;
        return extractedValue.getClass().getName();
    }

    protected Property getJenaProperty(){
        return model.createProperty(getRdfProperty().value());
    }

    protected RdfProperty getRdfProperty(){
        return memberWrapper.getAnnotation(RdfProperty.class);
    }

    protected String getMemberPath(){
        RdfProperty rdfPropertyInfo = getRdfProperty();
        RdfId rdfIdInfo = memberWrapper.getAnnotation(RdfId.class);

        if(rdfPropertyInfo!=null && !rdfPropertyInfo.path().isEmpty())
            return rdfPropertyInfo.path();
        else if(rdfIdInfo!=null && !rdfIdInfo.path().isEmpty())
            return rdfIdInfo.path();

        return "";
    }
}
