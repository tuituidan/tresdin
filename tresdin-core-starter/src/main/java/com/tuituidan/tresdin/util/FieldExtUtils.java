package com.tuituidan.tresdin.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public static Object getFieldValue(Field field, Object model) {
        try {
            ReflectionUtils.makeAccessible(field);
            return field.get(model);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Field[] getFields(Class<?> cls) {
        List<Field> allFields = new ArrayList<>();
        for (Class<?> currentClass = cls; currentClass != null; currentClass = currentClass.getSuperclass()) {
            Field[] fields = getModelFieldsInCache(currentClass);
            Collections.addAll(allFields, fields);
        }
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
}
