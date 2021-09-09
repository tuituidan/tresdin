package com.tuituidan.tresdin.mybatis;

import static com.github.pagehelper.page.PageMethod.clearPage;
import static com.github.pagehelper.page.PageMethod.offsetPage;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.mybatis.bean.PageData;
import com.tuituidan.tresdin.mybatis.bean.PageParam;
import com.tuituidan.tresdin.util.StringExtUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Transient;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * 查询封装.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@UtilityClass
public class QueryHelper {

    /**
     * 查询数量.
     *
     * @param supplier supplier
     * @param <T>      T
     * @return long
     */
    public static <T> long count(Supplier<T> supplier) {
        return PageMethod.count(supplier::get);
    }

    /**
     * 设置排序.
     *
     * @param sort sort
     */
    public static void orderBy(String sort) {
        PageMethod.orderBy(formatSort(sort));
    }

    /**
     * 设置排序.
     *
     * @param sort sort
     * @param cls  cls
     */
    public static void orderBy(String sort, Class<?> cls) {
        PageMethod.orderBy(formatSort(sort, cls));
    }

    /**
     * 分页查询，不转换排序字段.
     *
     * @param pageParam 分页参数
     * @param supplier  执行方法
     * @param <T>       T
     * @return PageData PageData
     */
    public static <T> PageData<T> queryPage(PageParam pageParam, Supplier<T> supplier) {
        return queryPage(pageParam, supplier, null);
    }

    /**
     * 分页查询，转换排序字段.
     *
     * @param pageParam 分页参数
     * @param supplier  执行方法
     * @param cls       cls
     * @param <T>       T
     * @return PageData PageData
     */
    public static <T> PageData<T> queryPage(PageParam pageParam, Supplier<T> supplier, Class<?> cls) {
        Page<T> page = offsetPage(pageParam.getOffset(), pageParam.getLimit());
        page.setOrderBy(formatSort(pageParam.getSort(), cls));
        PageData<T> pageableData = new PageData<>();
        try {
            pageableData.setData(supplier.get());
        } finally {
            clearPage();
        }
        pageableData.setLimit(pageParam.getLimit());
        pageableData.setOffset(pageParam.getOffset());
        pageableData.setTotal(page.getTotal());
        return pageableData;
    }

    /**
     * 转换restful格式的排序字段.
     *
     * @param sort sort
     * @return string
     */
    public static String formatSort(String sort) {
        return formatSort(sort, null);
    }

    /**
     * 转换restful格式的排序字段.
     *
     * @param sort sort
     * @param cls  cls
     * @return string
     */
    public static String formatSort(String sort, Class<?> cls) {
        if (StringUtils.isBlank(sort)) {
            return sort;
        }
        return Arrays.stream(sort.split(Separator.COMMA)).map(fieldName -> {
            String direction = " asc nulls last";
            if (fieldName.startsWith(Separator.HYPHEN)) {
                fieldName = StringUtils.substringAfter(fieldName, Separator.HYPHEN);
                direction = " desc nulls last";
            }
            if (cls == null) {
                return fieldName + direction;
            }
            Field field = FieldUtils.getField(cls, fieldName, true);
            if (field == null) {
                return fieldName + direction;
            }
            Column column = AnnotationUtils.findAnnotation(field, Column.class);
            if (column == null) {
                Assert.notNull(AnnotationUtils.findAnnotation(field, Transient.class),
                        StringExtUtils.format("无法找到排序字段{}对应的数据库字段", fieldName));
                return fieldName + direction;
            }
            return column.name() + direction;
        }).collect(Collectors.joining(Separator.COMMA));
    }
}
