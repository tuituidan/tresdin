package com.tuituidan.tresdin.mybatis;

import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.util.StringExtUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.Column;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * SortBuilder.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/30
 */
public class SortBuilder {

    private final String sort;

    private Class<?> cls;

    private String nullBehavior;

    private boolean isCamelCase = false;

    /**
     * 构造函数
     *
     * @param sort sort
     */
    public SortBuilder(String sort) {
        this.sort = sort;
    }

    /**
     * 添加实体类用于查询排序字段
     *
     * @param cls cls
     * @return SortBuilder
     */
    public SortBuilder withEntity(Class<?> cls) {
        this.cls = cls;
        return this;
    }

    /**
     * 是否进行驼峰命名转下划线命名
     *
     * @return SortBuilder
     */
    public SortBuilder isCamelCase() {
        this.isCamelCase = true;
        return this;
    }

    /**
     * 是否进行驼峰命名转下划线命名
     *
     * @param isCamelCase true
     * @return SortBuilder
     */
    public SortBuilder isCamelCase(boolean isCamelCase) {
        this.isCamelCase = isCamelCase;
        return this;
    }

    /**
     * 设置null的默认排序
     *
     * @param nullBehavior null的默认排序
     * @return SortBuilder
     */
    public SortBuilder nullBehavior(String nullBehavior) {
        this.nullBehavior = nullBehavior;
        return this;
    }

    /**
     * 构建排序字段
     *
     * @return string
     */
    public String build() {
        if (StringUtils.isBlank(sort)) {
            return sort;
        }
        return Arrays.stream(sort.split(Separator.COMMA)).map(fieldName -> {
            String direction = "";
            if (fieldName.startsWith(Separator.HYPHEN)) {
                fieldName = StringUtils.substringAfter(fieldName, Separator.HYPHEN);
                direction = " desc " + StringUtils.defaultString(nullBehavior);
            }
            if (cls == null) {
                return toUnderLineCase(fieldName) + direction;
            }
            Field field = FieldUtils.getField(cls, fieldName, true);
            if (field == null) {
                return toUnderLineCase(fieldName) + direction;
            }
            Column column = AnnotationUtils.findAnnotation(field, Column.class);
            if (column == null) {
                return toUnderLineCase(fieldName) + direction;
            }
            return column.name() + direction;
        }).collect(Collectors.joining(Separator.COMMA));
    }

    private String toUnderLineCase(String fieldName) {
        if (isCamelCase) {
            return StringExtUtils.camelToUnderLineCase(fieldName);
        }
        return fieldName;
    }

    /**
     * 创建SortBuilder
     *
     * @param sort sort
     * @return SortBuilder
     */
    public static SortBuilder create(String sort) {
        return new SortBuilder(sort);
    }

}
