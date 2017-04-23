package de.fraunhofer.iais.eis.jrdfb.util;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.IAIS;
import org.apache.commons.lang3.ClassUtils;
import org.apache.jena.ext.com.google.common.collect.Lists;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

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
     * @param getter the getter Method
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
     * @param getter the getter Method
     * @param clazz the class where the property is declared
     * @return Property
     *
     * Takes getter Method and returns property which it returns
     */
    public static Field getProperty(@NotNull Method getter, Class<?> clazz)
            throws NoSuchFieldException {
        PropertyDescriptor desc = getPropertyDescriptor(getter);

        if(desc != null){
            String name =  desc.getName();
            return clazz.getDeclaredField(name);
        }

        throw new NoSuchFieldException("There is no such field against getter method '"
                + getter.getName()+"'");
    }

    /**
     * @param getter the getter Method
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
     * @param getter the getter Method
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
        } catch (IntrospectionException e)
        {
            throw new NoSuchElementException(e.getMessage());
        }

        return null;
    }

    /**
     * @param clazz
     * @param value
     * @return Object
     */
    public static Object toObject( Class clazz, String value ) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        if( UUID.class == clazz ) return UUID.fromString(value);
        if( LocalDateTime.class == clazz ) return LocalDateTime.parse(value);

        Constructor<?> cons = clazz.getDeclaredConstructor(String.class);
        cons.setAccessible(true);
        return cons.newInstance(value);

    }

    /**
     * @param className
     * @param text
     * @return
     */
    public static Object stringToObject(String className, String text)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        Class<?> targetType = org.springframework.util.ClassUtils.forName(className, null);
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
                return tmpClass.getDeclaredField(fieldName);
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

        if (nestedField != null) {
            nestedField.set(nestedBean, toObject(nestedField.getType(), value.toString()));
        }

        return nestedBean;
    }

    /**
     * Get all the fields in the hierarchy until exclusiveParent. exclusiveParent fields are not
     * included.
     * If the same property is annotated more than one in the hierarchy, field which is present on
     * the lowest level with that property is included.
     * @param startClass
     * @param exclusiveParent
     * @return
     */
    public static List<Field> getFieldsUpTo(@NotNull Class<?> startClass,
                                                @Nullable Class<?> exclusiveParent) {

        List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&
                (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields = getFieldsUpTo(parentClass, exclusiveParent);
            for(Field field: parentClassFields){
                if(field.isAnnotationPresent(RdfProperty.class)){
                    RdfProperty property = field.getAnnotation(RdfProperty.class);

                    // if the same property is annotated already do not add field from parent
                    // with the same property
                    if(currentClassFields
                            .stream()
                            .noneMatch(f -> f.isAnnotationPresent(RdfProperty.class)
                                            && f.getAnnotation(RdfProperty.class)
                                                .value().equals(property.value() )
                                            && f.getType() == field.getType()
                            )){
                        currentClassFields.add(field);
                    }

                }else{
                    currentClassFields.add(field);
                }
            }
        }

        return currentClassFields;
    }

    /**
     * Gets an array of all methods in a class hierarchy walking up to parent classes
     *
     * If the same property is annotated more than one in the hierarchy, field which is present on
     * the lowest level with that property is included.
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
            // if the same property is annotated already do not add method from parent
            // with the same property
            for (Method method: superClassMethods){
                if(method.isAnnotationPresent(RdfProperty.class)){
                    RdfProperty property = method.getAnnotation(RdfProperty.class);
                    if(Arrays.stream(declaredMethods)
                            .noneMatch(m -> m.isAnnotationPresent(RdfProperty.class)
                                    && m.getAnnotation(RdfProperty.class)
                                    .value().equals(property.value())
                                    && m.getReturnType() == method.getReturnType()
                            )){
                        allMethods.add(method);
                    }
                }else{
                    allMethods.add(method);
                }
            }
            //allMethods.addAll(superClassMethods);
        }
        allMethods.addAll(Arrays.asList(declaredMethods));
        return allMethods;
    }

    /**
     * Returns the matching Class if class with @param className is assignable from any class in the
     * array else null
     *
     * @param array the array of classes
     * @param className the class name to be found in the array
     * @return returns the Class if it exists in array
     */
    public static Class<?> getIfAssignableFromAny(Class[] array, String className)
            throws ClassNotFoundException {
        if(className == null || className.isEmpty()) return null;
        Class<?> targetClass = Class.forName(className);
        List<Class<?>> classes = Arrays.asList(array);

        if(classes.contains(targetClass)) return targetClass;

        for ( Class<?> clazz : ClassUtils.hierarchy(targetClass, ClassUtils.Interfaces.INCLUDE)){
            if(classes.contains(clazz)) return clazz;
        }
        return null;
    }


    /**
     * Takes the class as parameter and returns the instance of the class
     *
     * @param clazz class whose instance has to be initialized
     * @return returns the instance of the class
     */
    @NotNull
    public static Object initClassInstance(@NotNull Class<?> clazz){
        Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
        return objenesis.newInstance(clazz);
    }


    /**
     * Takes the class as parameter and returns the instance of the class by invoking default
     * constructor
     *
     * @param clazz class whose instance has to be initialized
     * @return returns the instance of the class
     */
    @NotNull
    public static Object initClassDefaultInstance(@NotNull Class<?> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
                InstantiationException {
        Constructor<?> cons = clazz.getDeclaredConstructor();
        cons.setAccessible(true);
        return cons.newInstance();
    }

    /**
     * Takes the class name as parameter and returns the instance of the class
     *
     * @param className class name whose instance has to be initialized
     * @return returns the instance of the class
     */
    @NotNull
    public static Object initClassInstance(String className) throws ClassNotFoundException {
        Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
        Class<?> targetType = Class.forName(className);
        return objenesis.newInstance(targetType);
    }

    /**
     * Takes the class name as parameter and returns the instance of the class by invoking default
     * constructor
     * @param className class name whose instance has to be initialized
     * @return returns the instance of the class
     */
    @NotNull
    public static Object initClassDefaultInstance(String className)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
                    InvocationTargetException, InstantiationException {
        Class<?> targetType = Class.forName(className);
        if(targetType.getName().equals("java.util.Arrays$ArrayList")){
            targetType = ArrayList.class;
        }
        Constructor<?> cons = targetType.getDeclaredConstructor();
        cons.setAccessible(true);
        return cons.newInstance();
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


    public static Class<?> getResourceClass(Resource resource) throws ClassNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        Resource metadata = (Resource)resource
                                .getProperty(model.createProperty(IAIS.CLASS_MAPPING)).getObject();
        String rdfTypeProperty = ((Resource)resource.getProperty(RDF.type).getObject()).getURI();
        org.apache.jena.rdf.model.Statement metaProperty
                = metadata.getProperty(model.createProperty(rdfTypeProperty));

        return Class.forName(metaProperty.getObject().toString());
    }

}
