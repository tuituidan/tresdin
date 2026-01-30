package com.tuituidan.tresdin.mybatis;

import static com.github.pagehelper.page.PageMethod.clearPage;
import static com.github.pagehelper.page.PageMethod.offsetPage;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.tuituidan.tresdin.page.PageData;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

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
     * @param limit 每页条数
     * @return 总页数
     */
    public static int calcPageCount(long total, int limit) {
        if (total == 0) {
            return 0;
        }
        if (total <= limit) {
            return 1;
        }
        return (int) (total + limit - 1) / limit;
    }

    /**
     * 遍历所有分页数据
     *
     * @param limit 分页大小
     * @param supplier 查询方法
     * @param consumer 回调方法
     * @param <T> T
     */
    public static <T> void iteratePageList(int limit, Supplier<T> supplier, Consumer<PageData<T>> consumer) {
        long total = count(supplier);
        int pageCount = calcPageCount(total, limit);
        if (pageCount <= 0) {
            PageData<T> pageData = new PageData<>();
            pageData.setLimit(limit);
            pageData.setPageCount(pageCount);
            pageData.setTotal(total);
            consumer.accept(pageData);
            return;
        }
        for (int i = 0; i < pageCount; i++) {
            consumer.accept(queryPage(i * limit, limit, supplier, false));
        }
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
        PageMethod.orderBy(SortBuilder.create(sort).build());
    }

    /**
     * 设置排序.
     *
     * @param sort sort
     * @param cls cls
     */
    public static void orderBy(String sort, Class<?> cls) {
        PageMethod.orderBy(SortBuilder.create(sort).withEntity(cls).build());
    }

    /**
     * 设置排序.
     *
     * @param sort sort
     * @param isCamelCase 是否进行驼峰命名转下划线命名
     */
    public static void orderBy(String sort, boolean isCamelCase) {
        PageMethod.orderBy(SortBuilder.create(sort).isCamelCase(isCamelCase).build());
    }

    /**
     * 批量设置排序.
     *
     * @param sortBuilder sortBuilder
     */
    public static void orderBy(SortBuilder sortBuilder) {
        PageMethod.orderBy(sortBuilder.build());
    }

    /**
     * 分页查询.
     *
     * @param offset 分页参数offset
     * @param limit 分页参数limit
     * @param supplier 执行方法
     * @param <T> T
     * @return PageData PageData
     */
    public static <T> PageData<T> queryPage(int offset, int limit, Supplier<T> supplier) {
        return queryPage(offset, limit, supplier, true);
    }

    /**
     * 分页查询.
     *
     * @param offset 分页参数offset
     * @param limit 分页参数limit
     * @param supplier 执行方法
     * @param doCount 执行总数统计
     * @param <T> T
     * @return PageData PageData
     */
    public static <T> PageData<T> queryPage(int offset, int limit, Supplier<T> supplier, boolean doCount) {
        PageData<T> pageData = new PageData<>();
        Page<T> page = offsetPage(offset, limit, doCount);
        try {
            pageData.setData(supplier.get());
        } finally {
            clearPage();
        }
        pageData.setTotal(page.getTotal());
        pageData.setLimit(limit);
        pageData.setOffset(offset);
        pageData.setPageCount(page.getPages());
        pageData.setPageIndex(page.getPageNum());
        pageData.setIndex(offset + 1);
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
        BeanExtUtils.copyProperties(pageData, result, "offset", "limit", "total", "index", "pageCount",
                "pageIndex", "customData");
        return result;
    }

}
