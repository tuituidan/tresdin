package com.tuituidan.tresdin.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * ListUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/7
 */
@UtilityClass
public class ListExtUtils {

    /**
     * 拆出要删除的和要插入的
     *
     * @param saveIds saveIds
     * @param existIds existIds
     * @return Pair
     */
    public <T> Pair<Set<T>, Set<T>> splitSaveIds(T[] saveIds, Set<T> existIds) {
        Set<T> insertIds = ArrayUtils.isEmpty(saveIds) ? new HashSet<>() :
                Arrays.stream(saveIds).collect(Collectors.toSet());
        Set<T> deleteIds = new HashSet<>(existIds);
        deleteIds.removeAll(insertIds);
        // 要保存的去掉现有的，就是要新增的
        insertIds.removeAll(existIds);
        return Pair.of(deleteIds, insertIds);
    }

}
