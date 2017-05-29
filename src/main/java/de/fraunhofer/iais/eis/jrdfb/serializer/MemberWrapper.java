package de.fraunhofer.iais.eis.jrdfb.serializer;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfId;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class MemberWrapper implements AnnotatedElement, Member{
    private AccessibleObject member;

    public MemberWrapper(Field field) {
        this.member = field;
        member.setAccessible(true);
    }

    public MemberWrapper(Method method) {
        this.member = method;
        member.setAccessible(true);
    }

    public MemberWrapper(AccessibleObject member){
        if(!(member instanceof Field) && !(member instanceof Method))
            throw new IllegalArgumentException("Only java.lang.reflect.Method" +
                    " or java.lang.reflect.Field are accepted as " +
                    "constructor parameter type");
        this.member = member;
        member.setAccessible(true);
    }

    private Object getValue(Object object) throws ReflectiveOperationException {
        if(member instanceof Field)
            return ((Field) member) .get(object);
        else if(member instanceof Method)
            return ((Method) member).invoke(object);
        return null;
    }

    public void setValue(Object object, Object value)
            throws ReflectiveOperationException {
        if(member instanceof Field)
            ((Field) member) .set(object, value);
        else if(member instanceof Method ){
            try{
                (ReflectUtils.getSetterMethod((Method) member)).invoke(object, value);
            }catch(NoSuchMethodException e){
                Field property = ReflectUtils.getProperty((Method) member, object.getClass());
                property.setAccessible(true);
                property.set(object, value);
            }
        }
    }

    private Object getNestedObject(final Object bean, final String fieldPath)
            throws ReflectiveOperationException{
        if(member instanceof Field){
            String [] fieldNames = fieldPath.split("\\.");
            Field nestedField;
            Object nestedBean = ((Field) member).get(bean);
            for(String fieldName : fieldNames){
                nestedField = ReflectUtils.getFieldFromHierarchy(nestedBean.getClass(), fieldName);
                nestedField.setAccessible(true);
                nestedBean = nestedField.get(nestedBean);
            }
            return nestedBean;
        }
        return null;
    }

    public Object initNestedObject(final Object bean, final String
            fieldPath, Object value) throws ReflectiveOperationException{
        if(member instanceof Field){
            String [] fieldNames = fieldPath.split("\\.");
            Field nestedField = null;
            Object nestedBean = ((Field) member).get(bean);
            for(int i= 0; i < fieldNames.length; i++){
                if(nestedBean == null){
                    Constructor cons = ((Field) member).getType().getDeclaredConstructor();
                    cons.setAccessible(true);
                    nestedBean =  cons.newInstance();
                }
                nestedField = ReflectUtils.getFieldFromHierarchy(nestedBean.getClass(),
                        fieldNames[i]);
                nestedField.setAccessible(true);
                if(i + 1 < fieldNames.length ) nestedBean = nestedField.get(nestedBean);
            }

            if (nestedField != null) {
                nestedField.set(nestedBean, ReflectUtils.toObject(nestedField.getType(), value
                        .toString()));
            }

            return nestedBean;
        }

        return null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        if(member instanceof Method)
            return AnnotationUtils.findAnnotation((Method)member, annotationClass);
        else
            return member.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return member.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return member.getDeclaredAnnotations();
    }

    @Override
    public Class<?> getDeclaringClass() {
        if(member instanceof Field)
            return ((Field) member) .getDeclaringClass();
        else if(member instanceof Method)
            ((Method) member).getDeclaringClass();
        return null;
    }

    @Override
    public String getName() {
        if(member instanceof Field)
            return ((Field) member) .getName();
        else if(member instanceof Method)
            ((Method) member).getName();
        return null;
    }

    @Override
    public int getModifiers() {
        if(member instanceof Field)
            return ((Field) member) .getModifiers();
        else if(member instanceof Method)
            ((Method) member).getModifiers();
        return 0x0;
    }

    @Override
    public boolean isSynthetic() {
        if(member instanceof Field)
            return ((Field) member) .isSynthetic();
        else if(member instanceof Method)
            ((Method) member).isSynthetic();
        return false;
    }

    public Class<?> getType(){
        if(member instanceof Field)
            return ((Field) member) .getType();
        else if(member instanceof Method)
            return ((Method) member).getReturnType();
        return null;
    }

    public Type getGenericType(){
        if(member instanceof Field)
            return ((Field) member) .getGenericType();
        else if(member instanceof Method)
            return ((Method) member).getGenericReturnType();
        return null;
    }

    public String getMemberPath(){
        RdfProperty rdfPropertyInfo = this.getAnnotation(RdfProperty.class);
        RdfId rdfIdInfo = this.getAnnotation(RdfId.class);

        if(rdfPropertyInfo!=null && !rdfPropertyInfo.path().isEmpty())
            return rdfPropertyInfo.path();
        else if(rdfIdInfo!=null && !rdfIdInfo.path().isEmpty())
            return rdfIdInfo.path();

        return "";
    }

    public @Nullable Object getMemberValue(@NotNull Object object)
            throws ReflectiveOperationException {
        return getValue(object);
    }

    public void setMemberValue(@NotNull Object object, @NotNull Object value)
            throws ReflectiveOperationException {
        if(!ClassUtils.isAssignable(value.getClass(), getType())) return;
        setValue(object, value);
    }

    public Object extractMemberValue(Object object)
            throws ReflectiveOperationException {
        if(getMemberPath().isEmpty()){
            return getMemberValue(object);
        }else{
            return getNestedObject(object, getMemberPath());
        }
    }

    public Type getGenericTypeArgument(){
        ParameterizedType genericType =
                (ParameterizedType) this.getGenericType();
        return genericType.getActualTypeArguments()[0];
    }

    public RdfProperty getRdfProperty(){
        if(member instanceof Method){
            return AnnotationUtils
                    .findAnnotation((Method) member, RdfProperty.class);
        }
        else{
            return AnnotationUtils.findAnnotation(member, RdfProperty.class);
        }
    }

    public RdfMarshaller getRdfMarshaller(){
        if(member instanceof Method){
            return AnnotationUtils.findAnnotation((Method)
                    member, de.fraunhofer.iais.eis.jrdfb.annotation
                    .RdfMarshaller.class);
        }else{
            return AnnotationUtils.findAnnotation(member,
                    de.fraunhofer.iais.eis.jrdfb.annotation
                            .RdfMarshaller.class);
        }
    }
}
