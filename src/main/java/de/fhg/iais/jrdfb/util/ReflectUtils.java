package de.fhg.iais.jrdfb.util;

import org.apache.jena.ext.com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

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
    public static Method getSetterMethod(Method getter) throws NoSuchMethodException {
        PropertyDescriptor desc = getPropertyDescriptor(getter);

        if(desc != null && desc.getWriteMethod() != null)
            return desc.getWriteMethod();

        throw new NoSuchMethodException("There is no setter method against getter method '"
                + getter.getName()+"'");
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
    public static Object toObject( Class clazz, String value ) {
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
     * @param bean Bean which contain the memberWrapper
     * @param field Field whose nested memberWrapper is to be extracted
     * @param fieldPath Path of the nested memberWrapper
     * @return the nested memberWrapper value
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

    public static Field getFieldFromHierarchy(Class<?> clazz, String fieldName) {
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

    public static List<Field> getFieldsUpTo(@NotNull Class<?> startClass,
                                                @Nullable Class<?> exclusiveParent) {

        List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&
                (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields =
                    (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

    public static BigInteger getChecksum(Object obj) {
        if (obj == null) {
            return BigInteger.ZERO;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            MessageDigest m = MessageDigest.getInstance("SHA1");
            m.update(baos.toByteArray());

            return new BigInteger(1, m.digest());
        }catch (Exception e){
            return BigInteger.valueOf(obj.hashCode());
        }
    }

    /**
     * https://coderwall.com/p/wrqcsg/java-reflection-get-all-methods-in-hierarchy
     *
     * Gets an array of all methods in a class hierarchy walking up to parent classes
     * @param objectClass the class
     * @param exclusiveParent full stop class
     * @return the methods array
     */
    public static Set<Method> getAllMethodsInHierarchy(Class<?> objectClass,
                                                       Class<?> exclusiveParent) {
        Set<Method> allMethods = new HashSet<>();
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        if (objectClass.getSuperclass() != null && !(objectClass.getSuperclass().equals
                (exclusiveParent))) {
            Class<?> superClass = objectClass.getSuperclass();
            Set<Method> superClassMethods = getAllMethodsInHierarchy(superClass, exclusiveParent);
            allMethods.addAll(superClassMethods);
        }
        allMethods.addAll(Arrays.asList(declaredMethods));
        return allMethods;
    }

}
