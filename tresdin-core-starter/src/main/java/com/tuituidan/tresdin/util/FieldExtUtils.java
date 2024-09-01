package com.tuituidan.tresdin.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.Transient;
import lombok.experimental.UtilityClass;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/**
 * ClassUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@UtilityClass
public class FieldExtUtils {

    /**
     * getFieldValue
     *
     * @param field field
     * @param model model
     * @return Object
     */
    public static Object getFieldValue(Field field, Object model) {
        if (field == null) {
            return null;
        }
        try {
            ReflectionUtils.makeAccessible(field);
            return field.get(model);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * getFieldValue
     *
     * @param fieldName fieldName
     * @param model model
     * @return Object
     */
    public static Object getFieldValue(String fieldName, Object model) {
        Field field;
        Class<?> cls = model.getClass();
        do {
            Field[] fields = getModelFieldsInCache(cls);
            field = Arrays.stream(fields).filter(f -> f.getName().equals(fieldName)).findAny().orElse(null);
            if (field != null) {
                break;
            }
            cls = cls.getSuperclass();
        } while (cls != null && !Object.class.equals(cls));
        return getFieldValue(field, model);
    }

    /**
     * getFields
     *
     * @param cls cls
     * @return Field[]
     */
    public static Field[] getFields(Class<?> cls) {
        List<Field> allFields = new ArrayList<>();
        Class<?> curCls = cls;
        do {
            Field[] fields = getModelFieldsInCache(curCls);
            allFields.addAll(Arrays.asList(fields));
            curCls = curCls.getSuperclass();
        } while (curCls != null && !Object.class.equals(curCls));
        return allFields.toArray(new Field[0]);
    }

    private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentReferenceHashMap<>(1024);

    private static Field[] getModelFieldsInCache(Class<?> cls) {
        return FIELD_CACHE.computeIfAbsent(cls, modelClass -> {
            Field[] fields = modelClass.getDeclaredFields();
            return Arrays.stream(fields).filter(f -> !f.isSynthetic()).filter(f -> {
                Transient notColumn = f.getAnnotation(Transient.class);
                return !"serialVersionUID".equals(f.getName()) && notColumn == null;
            }).toArray(Field[]::new);
        });
    }

    private static final Map<Class<?>, Field> FIELD_KEY_CACHE = new ConcurrentReferenceHashMap<>(1024);

    /**
     * getModelKeyInCache
     *
     * @param cls cls
     * @param key key
     * @return Field
     */
    public static Field getModelKeyInCache(Class<?> cls, String key) {
        return FIELD_KEY_CACHE.computeIfAbsent(cls, modelClass ->
                Arrays.stream(modelClass.getDeclaredFields())
                        .filter(it -> key.equals(it.getName()))
                        .findFirst().orElseThrow(NullPointerException::new)
        );
    }

}
