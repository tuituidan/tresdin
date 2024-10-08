package com.tuituidan.tresdin.page;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * PageData.
 *
 * @author tuituidan
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageData<T> {

    private int offset = 0;

    private int limit = 15;

    private long total = -1;

    private int index;

    private int pageCount;

    private int pageIndex;

    private T data;

    private Map<?, ?> customData = new HashMap<>();

}
