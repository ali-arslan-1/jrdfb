package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.IAIS;
import org.apache.commons.lang3.ClassUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public abstract class BasePropUnmarshaller implements PropertyUnmarshaller {
    protected MemberWrapper memberWrapper;
    protected Model model;
    protected RdfSerializer rdfSerializer;

    public BasePropUnmarshaller(Field field, RdfSerializer rdfSerializer) {
        this.memberWrapper = new MemberWrapper(field);
        this.model = rdfSerializer.model;
        this.rdfSerializer = rdfSerializer;
    }

    public BasePropUnmarshaller(Method method, RdfSerializer rdfSerializer) {
        this.memberWrapper = new MemberWrapper(method);
        this.model = rdfSerializer.model;
        this.rdfSerializer = rdfSerializer;
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

    @Override
    @Nullable
    public String resolveMemberClassName(@NotNull Resource resource)
            throws ReflectiveOperationException{
        Resource metadata = (Resource)resource
                                .getProperty(model.createProperty(IAIS.CLASS_MAPPING)).getObject();
        Statement metaProperty = metadata.getProperty(getJenaProperty());
        if(metaProperty==null)
            return null;

        return metaProperty.getObject().toString();
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
