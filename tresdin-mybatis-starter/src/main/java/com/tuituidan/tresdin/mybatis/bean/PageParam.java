package com.tuituidan.tresdin.mybatis.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("分页参数")
public class PageParam {

    @ApiModelProperty(value = "分页大小", required = true, example = "10")
    private Integer limit;

    @ApiModelProperty(value = "分页起始", required = true, example = "0")
    private Integer offset;

    @ApiModelProperty("排序字段")
    private String sort;

}
