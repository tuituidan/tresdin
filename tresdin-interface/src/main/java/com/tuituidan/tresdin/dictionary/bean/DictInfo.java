package com.tuituidan.tresdin.dictionary.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DictInfo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/23
 */
@Getter
@Setter
@Accessors(chain = true)
public class DictInfo {

    private String id;

    private String pid;

    private String name;

    private String level;

    private Integer order;

    private String valid;
}

