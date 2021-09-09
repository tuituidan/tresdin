package com.tuituidan.tresdin.mybatis.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * PageData.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageData<T> {
    private int offset = 0;
    private int limit = 15;
    private long total = -1;
    private T data;
    private Map<?, ?> customData = new HashMap<>();
}
