package de.fraunhofer.iais.eis.jrdfb.resolver;

import de.fraunhofer.iais.eis.jrdfb.util.ReflectUtils;
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
    }

    public MemberWrapper(Method method) {
        this.member = method;
    }

    public AccessibleObject getMember(){
        return member;
    }

    public Object getValue(Object object) throws ReflectiveOperationException {
        if(member instanceof Field)
            return ((Field) member) .get(object);
        else if(member instanceof Method)
            return ((Method) member).invoke(object);
        return null;
    }

    public void setValue(Object object, Object value) throws ReflectiveOperationException {
        if(member instanceof Field)
            ((Field) member) .set(object, value);
        else if(member instanceof Method)
            (ReflectUtils.getSetterMethod((Method) member)).invoke(object, value);
    }

    public Object getNestedObject(final Object bean, final String fieldPath)
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

            nestedField.set(nestedBean, ReflectUtils.toObject(nestedField.getType(), value
                    .toString()));

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

    public Type getType(){
        if(member instanceof Field)
            return ((Field) member) .getType();
        else if(member instanceof Method)
            return ((Method) member).getReturnType();
        return null;
    }
}
