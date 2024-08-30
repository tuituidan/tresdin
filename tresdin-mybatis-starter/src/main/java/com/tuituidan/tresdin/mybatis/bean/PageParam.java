package com.tuituidan.tresdin.mybatis.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * PageParam.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageParam {

    private Integer limit;

    private Integer offset;

    private String sort;

}
