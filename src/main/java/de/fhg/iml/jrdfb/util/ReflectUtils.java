package de.fhg.iml.jrdfb.util;

import java.beans.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class ReflectUtils {

    /**
     * @param getter
     * @return Property name
     *
     * Takes getter Method and returns property name which it returns
     */
    public static String getPropertyName(Method getter)
    {
        PropertyDescriptor desc = getPropertyDescriptor(getter);

        if(desc != null)
            return desc.getName();

        return null;
    }

    /**
     * @param getter
     * @return write method
     *
     * Takes getter Method and returns setter method
     */
    public static Method getSetterMethod(Method getter) throws Exception {
        PropertyDescriptor desc = getPropertyDescriptor(getter);

        if(desc != null)
            return desc.getWriteMethod();

        throw new Exception("There is no setter method for getter method " + getter.getName());
    }


    /**
     * @param getter
     * @return PropertyDescriptor
     *
     * Takes getter Method and returns corresponding PropertyDescriptor
     */
    public static PropertyDescriptor getPropertyDescriptor(Method getter)
    {
        try
        {
            Class<?> clazz=getter.getDeclaringClass();
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props)
            {
                if(getter.equals(pd.getWriteMethod()) || getter.equals(pd.getReadMethod()))
                {
                    return pd;
                }
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param clazz
     * @param value
     * @return Object
     */
    private static Object toObject( Class clazz, String value ) {
        if( UUID.class == clazz ) return UUID.fromString(value);
        if( LocalDateTime.class == clazz ) return LocalDateTime.parse(value);

        try {
            Constructor<?> cons = clazz.getDeclaredConstructor(String.class);
            cons.setAccessible(true);
            return cons.newInstance(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * @param className
     * @param text
     * @return
     */
    public static Object stringToObject(String className, String text)
            throws ClassNotFoundException {
        Class<?> targetType = Class.forName(className);
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        if(editor==null) return toObject(targetType, text);
        editor.setAsText(text);
        return editor.getValue();
    }


    /**
     * @param bean Bean which contain the field
     * @param field Field whose nested field is to be extracted
     * @param fieldPath Path of the nested field
     * @return the nested field value
     */
    public static Object getNestedField(final Object bean, final Field field, final String
            fieldPath)
            throws NoSuchFieldException, IllegalAccessException {
        String [] fieldNames = fieldPath.split("\\.");
        Field nestedField;
        Object nestedBean = field.get(bean);
        for(String fieldName : fieldNames){
            nestedField = getFieldFromHierarchy(nestedBean.getClass(), fieldName);
            nestedField.setAccessible(true);
            nestedBean = nestedField.get(nestedBean);
        }
        return nestedBean;
    }

    private static Field getFieldFromHierarchy(Class<?> clazz, String fieldName) {
        Class<?> tmpClass = clazz;
        do {
            try {
                Field f = tmpClass.getDeclaredField(fieldName);
                return f;
            } catch (NoSuchFieldException e) {
                tmpClass = tmpClass.getSuperclass();
            }
        } while (tmpClass != null);

        throw new RuntimeException("Field '" + fieldName
                + "' not found on class " + clazz);
    }

    public static Object initNestedField(final Object bean, final Field field, final String
            fieldPath, Object value) throws IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchMethodException, ClassNotFoundException {

        String [] fieldNames = fieldPath.split("\\.");
        Field nestedField = null;
        Object nestedBean = field.get(bean);
        for(int i= 0; i < fieldNames.length; i++){
            if(nestedBean == null){
                Constructor cons = field.getType().getDeclaredConstructor();
                cons.setAccessible(true);
                nestedBean =  cons.newInstance();
            }
            nestedField = getFieldFromHierarchy(nestedBean.getClass(), fieldNames[i]);
            nestedField.setAccessible(true);
            if(i + 1 < fieldNames.length ) nestedBean = nestedField.get(nestedBean);
        }

        nestedField.set(nestedBean, toObject(nestedField.getType(), value.toString()));

        return nestedBean;
    }

}
