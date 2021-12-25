package com.tuituidan.tresdin.mybatis;

import static com.github.pagehelper.page.PageMethod.clearPage;
import static com.github.pagehelper.page.PageMethod.offsetPage;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.mybatis.bean.PageParam;
import com.tuituidan.tresdin.page.PageData;
import com.tuituidan.tresdin.util.StringExtUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
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
     * 计算页数.
     *
     * @param total 数据总条数
     * @param pageSize 每页条数
     * @return 总页数
     */
    public static int calcPageCount(long total, int pageSize) {
        if (total == 0) {
            return 0;
        }
        if (total <= pageSize) {
            return 1;
        }
        return (int) (total + pageSize - 1) / pageSize;
    }

    /**
     * 查询数量.
     *
     * @param supplier supplier
     * @param <T> T
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
     * @param cls cls
     */
    public static void orderBy(String sort, Class<?> cls) {
        PageMethod.orderBy(formatSort(sort, cls));
    }

    /**
     * 分页查询，不转换排序字段.
     *
     * @param pageParam 分页参数
     * @param supplier 执行方法
     * @param <T> T
     * @return PageData PageData
     */
    public static <T> PageData<T> queryPage(PageParam pageParam, Supplier<T> supplier) {
        return queryPage(pageParam, supplier, null);
    }

    /**
     * 分页查询，转换排序字段.
     *
     * @param pageParam 分页参数
     * @param supplier 执行方法
     * @param cls cls
     * @param <T> T
     * @return PageData PageData
     */
    public static <T> PageData<T> queryPage(PageParam pageParam, Supplier<T> supplier, Class<?> cls) {
        Page<T> page = offsetPage(pageParam.getOffset(), pageParam.getLimit());
        page.setOrderBy(formatSort(pageParam.getSort(), cls));
        PageData<T> pageData = new PageData<>();
        try {
            pageData.setData(supplier.get());
        } finally {
            clearPage();
        }
        pageData.setLimit(pageParam.getLimit());
        pageData.setOffset(pageParam.getOffset());
        pageData.setTotal(page.getTotal());
        return pageData;
    }

    /**
     * 转换分页结果集
     *
     * @param pageData 原结果集
     * @param transFunc 数据转换方法
     * @param <T> T
     * @param <R> R
     * @return PageData
     */
    public static <T, R> PageData<List<R>> mapPage(PageData<List<T>> pageData,
            Function<T, R> transFunc) {
        PageData<List<R>> result = new PageData<>();
        result.setData(pageData.getData().stream().map(transFunc).collect(Collectors.toList()));
        result.setLimit(pageData.getLimit());
        result.setOffset(pageData.getOffset());
        result.setTotal(pageData.getTotal());
        return result;
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
     * @param cls cls
     * @return string
     */
    public static String formatSort(String sort, Class<?> cls) {
        if (StringUtils.isBlank(sort)) {
            return sort;
        }
        return Arrays.stream(sort.split(Separator.COMMA)).map(fieldName -> {
            // pg排序默认就是（升序） asc nulls last
            String direction = "";
            if (fieldName.startsWith(Separator.HYPHEN)) {
                fieldName = StringUtils.substringAfter(fieldName, Separator.HYPHEN);
                direction = " desc nulls last";
            }
            Column column = getColumn(fieldName, cls);
            if (column == null) {
                return fieldName + direction;
            }
            return column.name() + direction;
        }).collect(Collectors.joining(Separator.COMMA));
    }

    private static Column getColumn(String fieldName, Class<?> cls) {
        if (cls == null) {
            return null;
        }
        Field field = FieldUtils.getField(cls, fieldName, true);
        if (field == null) {
            return null;
        }
        Column column = AnnotationUtils.findAnnotation(field, Column.class);
        if (column == null) {
            Assert.notNull(AnnotationUtils.findAnnotation(field, Transient.class),
                    StringExtUtils.format("无法找到排序字段{}对应的数据库字段", fieldName));
            return null;
        }
        return column;
    }

}
