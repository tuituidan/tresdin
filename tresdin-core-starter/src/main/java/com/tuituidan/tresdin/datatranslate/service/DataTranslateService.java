package com.tuituidan.tresdin.datatranslate.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuituidan.tresdin.datatranslate.annotation.DeepTranslate;
import com.tuituidan.tresdin.datatranslate.annotation.TranslateToString;
import com.tuituidan.tresdin.datatranslate.bean.TranslationParameter;
import com.tuituidan.tresdin.datatranslate.translator.ITranslator;
import com.tuituidan.tresdin.page.PageData;
import com.tuituidan.tresdin.util.FieldExtUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IDataTranslateService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Service
@Slf4j
public class DataTranslateService {

    public static final String TRANSLATE_TEXT = "TranslateText";

    /**
     * 存储翻译器bean实例.
     */
    @Autowired(required = false)
    private List<ITranslator<?>> registeredTranslators;

    /**
     * key为该翻译器对应注解类型，value为翻译器实例.
     */
    private Map<Class<?>, ITranslator<?>> translatorMap;

    @PostConstruct
    private void init() {
        if (CollectionUtils.isEmpty(registeredTranslators)) {
            return;
        }
        translatorMap = new HashMap<>(registeredTranslators.size());
        for (ITranslator<?> translator : registeredTranslators) {
            translatorMap.put(((Class<?>) (((ParameterizedType) translator.getClass()
                    .getGenericInterfaces()[0]).getActualTypeArguments()[0])), translator);
        }
    }

    /**
     * handleValue
     *
     * @param obj obj
     * @return Object
     */
    public Object handleValue(Object obj) {
        // 如果是分页对象,则只把其中的数据对象翻译
        if (obj instanceof PageData) {
            PageData pd = (PageData) obj;
            Collection<?> data = (Collection<?>) pd.getData();
            pd.setData(handleCollection(data));
            return pd;
        }
        // 列表的话,转发至handleCollection
        if (obj instanceof Collection<?>) {
            return handleCollection((Collection<?>) obj);
        }

        if (obj instanceof Object[]) {
            return handleArray((Object[]) obj);
        }

        // Model 转发到handleOne
        return handleOne(obj);
    }

    /**
     * handleCollection
     *
     * @param collection collection
     * @return Collection
     */
    public Collection<Object> handleCollection(Collection<?> collection) {
        return collection.stream().map(this::handleOne).collect(Collectors.toList());
    }

    /**
     * handleArray
     *
     * @param collection collection
     * @return Collection
     */
    public Collection<Object> handleArray(Object[] collection) {
        return Arrays.stream(collection).map(this::handleOne).collect(Collectors.toList());
    }

    /**
     * handleOne
     *
     * @param obj obj
     * @return Object
     */
    public Object handleOne(final Object obj) {
        if (!isParseableObject(obj)) {
            return obj;
        }
        final boolean deep = obj.getClass().getAnnotation(DeepTranslate.class) != null;
        final JSONObject rawResult = JSON.parseObject(JSON.toJSONString(obj));
        if (rawResult == null) {
            return obj;
        }
        Deque<ValueWrapper> allStatck = new ArrayDeque<>();
        allStatck.push(new ValueWrapper(null, obj, rawResult, ParseType.OBJECT, obj));
        while (!allStatck.isEmpty()) {
            ValueWrapper wrapper = allStatck.removeFirst();
            try {
                switch (wrapper.getParseType()) {
                    case OBJECT:
                        handleOne(wrapper, allStatck);
                        break;
                    case COLLECTION_FIELD:
                        translateCollectionField(wrapper, allStatck, deep);
                        break;
                    case OBJECT_FIELD:
                        translateObjectField(wrapper, allStatck, deep);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error(String.format("翻译出错！字段：%s;值：%s。", wrapper.getField(), wrapper.getValue()), e);
            }
        }

        return rawResult;
    }

    private void handleOne(ValueWrapper valueWrapper, Deque<ValueWrapper> allStatck) {
        Object obj = valueWrapper.getValue();
        try {
            final Field[] fields = FieldExtUtils.getFields(obj.getClass());
            for (final Field field : fields) {
                final Object fieldValue = FieldExtUtils.getFieldValue(field, obj);
                if (fieldValue == null) {
                    continue;
                }
                ParseType parseType = ParseType.OBJECT_FIELD;
                if (fieldValue instanceof Collection<?> || fieldValue.getClass().isArray()) {
                    parseType = ParseType.COLLECTION_FIELD;
                }
                allStatck.addFirst(new ValueWrapper(field, fieldValue, valueWrapper.getResult(), parseType, obj));
            }
        } catch (Exception e) {
            log.error("处理代码或者组织机构翻译出错！" + obj.getClass(), e);
        }
    }

    private void translateCollectionField(ValueWrapper valueWrapper, Deque<ValueWrapper> allStatck, boolean deep) {
        Field field = valueWrapper.getField();
        Object value = valueWrapper.getValue();
        JSONObject result = valueWrapper.getResult();
        final String fieldName = field.getName();

        Annotation translatorAnnotation = getTranslatorAnnotation(field);
        if (translatorAnnotation != null) {
            TranslateToString translateToStringAnnotation = field.getAnnotation(TranslateToString.class);
            Collector collector = Objects.nonNull(translateToStringAnnotation)
                    ? Collectors.joining(translateToStringAnnotation.separator()) : Collectors.toList();
            ITranslator<?> translator = translatorMap.get(translatorAnnotation.annotationType());
            Object resultList =
                    compatibleStream(value)
                            .map(e -> translator.translate(
                                    new TranslationParameter(translatorAnnotation, e, valueWrapper.getObj())))
                            .collect(collector);
            result.put(fieldName + TRANSLATE_TEXT, resultList);
        } else {
            if (deep && !(value instanceof JSONArray)) {
                // 根据集合中的第一个数据来判断集合中存储的数据类型
                final Object collFirstData = getCollectionFirstData(value);
                if (!isParseableObject(collFirstData)) {
                    return;
                }
                JSONArray array = new JSONArray();
                compatibleStream(value).forEach(obj -> {
                    Object subResult = JSON.toJSON(obj);
                    if (subResult instanceof JSONObject) {
                        array.add(subResult);
                        allStatck.addFirst(new ValueWrapper(null, obj, (JSONObject) subResult, ParseType.OBJECT, null));
                    }
                });
                result.put(fieldName, array);
            }
        }
    }

    private Stream<?> compatibleStream(Object value) {
        Stream<?> stream = null;
        if (value instanceof Collection) {
            stream = ((Collection) value).stream();
        } else if (value.getClass().isArray()) {
            stream = Arrays.stream((Object[]) value);
        } else {
            stream = ((JSONArray) JSON.toJSON(value)).stream();
        }
        return stream;
    }

    private void translateObjectField(ValueWrapper valueWrapper, Deque<ValueWrapper> allStatck, boolean deep) {
        Field field = valueWrapper.getField();
        Object value = valueWrapper.getValue();
        JSONObject result = valueWrapper.getResult();
        final String fieldName = field.getName();
        Annotation translatorAnnotation = getTranslatorAnnotation(field);
        if (translatorAnnotation != null) {
            ITranslator<?> translator = translatorMap.get(translatorAnnotation.annotationType());
            String translateText = translator.translate(new TranslationParameter(translatorAnnotation, value,
                    valueWrapper.getObj()));
            result.put(fieldName + TRANSLATE_TEXT, translateText);
        } else {
            if (deep && isParseableObject(value)) {
                Object subResult = JSON.toJSON(value);
                if (subResult instanceof JSONObject) {
                    result.put(fieldName, subResult);
                    allStatck.addFirst(new ValueWrapper(null, value, (JSONObject) subResult, ParseType.OBJECT, null));
                }
            }
        }
    }

    private Object getCollectionFirstData(Object coll) {
        // 集合有两种情况：Collection和Array，且handleOne中只对这两种类型设置ParseType.COLLECTION_FIELD进而调用到此方法
        if (coll instanceof Collection<?>) {
            return !((Collection<?>) coll).isEmpty() ? ((Collection<?>) coll).iterator().next() : null;
        } else if (coll.getClass().isArray()) {
            return Array.getLength(coll) > 0 ? Array.get(coll, 0) : null;
        }
        return null;
    }

    private boolean isParseableObject(Object obj) {
        return obj != null
                && !isPrimitiveObject(obj.getClass())
                && !(obj instanceof JSON)
                && !(obj instanceof Map || obj instanceof Collection)
                && !(obj instanceof PageData);
    }

    /**
     * isPrimitiveObject
     *
     * @param clazz clazz
     * @return boolean
     */
    public boolean isPrimitiveObject(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == Boolean.class
                || clazz == Character.class
                || clazz == Byte.class
                || clazz == Short.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Float.class
                || clazz == Double.class
                || clazz == BigInteger.class
                || clazz == BigDecimal.class
                || clazz == String.class
                || clazz == java.util.Date.class
                || clazz == java.sql.Date.class
                || clazz == java.sql.Time.class
                || clazz == java.sql.Timestamp.class
                || clazz == java.time.LocalDate.class
                || clazz == java.time.LocalTime.class
                || clazz == java.time.LocalDateTime.class
                || clazz.isEnum();
    }

    private Annotation getTranslatorAnnotation(Field field) {
        for (Class c : translatorMap.keySet()) {
            Annotation translatorAnnotation = field.getAnnotation(c);
            if (Objects.nonNull(translatorAnnotation)) {
                return translatorAnnotation;
            }
        }
        return null;
    }

    enum ParseType {
        /**
         * ParseType.
         */
        OBJECT, OBJECT_FIELD, COLLECTION_FIELD
    }

    static class ValueWrapper {

        Field field;

        Object value;

        JSONObject result;

        ParseType parseType;

        Object obj;

        ValueWrapper(Field field, Object value, JSONObject result, ParseType parseType, Object obj) {
            this.field = field;
            this.value = value;
            this.result = result;
            this.parseType = parseType;
            this.obj = obj;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public JSONObject getResult() {
            return result;
        }

        public void setResult(JSONObject result) {
            this.result = result;
        }

        ParseType getParseType() {
            return parseType;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

    }

}
