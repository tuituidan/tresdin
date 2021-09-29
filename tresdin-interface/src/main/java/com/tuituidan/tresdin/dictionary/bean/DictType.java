package com.tuituidan.tresdin.dictionary.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DictType.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/23
 */
@Getter
@Setter
@Accessors(chain = true)
public class DictType  {

    private String id;

    private String name;

    private String valid;

}
