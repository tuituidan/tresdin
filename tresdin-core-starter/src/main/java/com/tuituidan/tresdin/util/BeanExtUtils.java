package com.tuituidan.tresdin.util;

import com.tuituidan.tresdin.exception.NewInstanceException;
import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

/**
 * BeanExtUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@UtilityClass
public class BeanExtUtils {

    /**
     * 转换对象.
     *
     * @param source 源属性Dto
     * @param cls 目标属性Do
     * @param <T> 转换类型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> cls) {
        return convert(source, cls, (String) null);
    }

    /**
     * 转换对象.
     *
     * @param source 源属性Dto
     * @param cls 目标属性Do
     * @param ignoreProperties 要忽略的属性
     * @param <T> 转换类型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> cls, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T target = newInstanceByCls(cls);
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    /**
     * 拷贝指定属性.
     *
     * @param source source
     * @param target target
     * @param props props
     */
    public static void copyProperties(Object source, Object target, String... props) {
        Assert.notEmpty(props, "必须指定要拷贝的属性，否则请直接使用Spring的BeanUtils.copyProperties");
        BeanUtils.copyProperties(source, target,
                Arrays.stream(new BeanWrapperImpl(source).getPropertyDescriptors())
                        .map(FeatureDescriptor::getName)
                        .filter(name -> !ArrayUtils.contains(props, name))
                        .toArray(String[]::new));
    }

    /**
     * 判断两个对象指定属性值是否一致.
     *
     * @param source source
     * @param target target
     * @param keys keys
     * @return boolean
     */
    public static boolean equals(Object source, Object target, String... keys) {
        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        BeanWrapper tarWrapper = new BeanWrapperImpl(target);
        for (String key : keys) {
            String srcVal = Objects.toString(srcWrapper.getPropertyValue(key));
            String tarVal = Objects.toString(tarWrapper.getPropertyValue(key));
            if (StringUtils.equals(srcVal, tarVal)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 复制非空数据.
     *
     * @param source 源属性Dto
     * @param target 目标属性Do
     */
    public static void copyNotNullProperties(Object source, Object target) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        BeanUtils.copyProperties(source, target, Arrays.stream(src.getPropertyDescriptors()).filter(item ->
                Objects.isNull(item.getReadMethod()) || Objects.isNull(src.getPropertyValue(item.getName()))
        ).map(FeatureDescriptor::getName).toArray(String[]::new));
    }

    /**
     * List拷贝.
     *
     * @param sourceList 源集合
     * @param target 目标对象
     * @param <T> 目标对象类型
     * @return list
     */
    public static <T> List<T> convertList(List<?> sourceList, Class<T> target) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        return sourceList.stream().map(item -> convert(item, target)).collect(Collectors.toList());

    }

    private static <T> T newInstanceByCls(Class<T> cls) {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new NewInstanceException("转换错误-{}", cls.getName(), ex);
        }
    }

}
