package com.tuituidan.tresdin.util;

import com.tuituidan.tresdin.exception.NewInstanceException;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
     * @param cls    目标属性Do
     * @param <T>    转换类型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> cls) {
        return convert(source, cls, (String) null);
    }

    /**
     * 转换对象.
     *
     * @param source           源属性Dto
     * @param cls              目标属性Do
     * @param ignoreProperties 要忽略的属性
     * @param <T>              转换类型
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
     * @param props  props
     */
    public static void copyProperties(Object source, Object target, String... props) {
        Assert.notEmpty(props, "props不能为空，必须指定要拷贝的属性");
        BeanUtils.copyProperties(source, target,
                Arrays.stream(new BeanWrapperImpl(source).getPropertyDescriptors()).filter(pd -> !ArrayUtils
                        .contains(props, pd.getName()))
                        .map(FeatureDescriptor::getName).toArray(String[]::new));
    }

    /**
     * 判断两个对象指定属性值是否一致.
     *
     * @param source source
     * @param target target
     * @param keys   keys
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
        BeanUtils.copyProperties(source, target, Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null).toArray(String[]::new));
    }

    /**
     * List拷贝.
     *
     * @param source 源数组
     * @param target 目标对象
     * @param <T>    目标对象类型
     * @return list
     */
    public static <T> List<T> copyList(List<?> source, Class<T> target) {
        List<T> targetList = new ArrayList<>();
        if (CollectionUtils.isEmpty(source)) {
            return targetList;
        }
        source.forEach(item -> targetList.add(convert(item, target)));
        return targetList;

    }

    private static <T> T newInstanceByCls(Class<T> cls) {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new NewInstanceException("转换错误-{}", cls.getName(), ex);
        }
    }
}
