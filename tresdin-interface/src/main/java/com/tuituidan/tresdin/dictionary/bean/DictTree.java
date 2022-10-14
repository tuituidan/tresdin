package com.tuituidan.tresdin.dictionary.bean;

import java.util.List;
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
public class DictTree extends DictInfo {

    private List<DictTree> children;

}

