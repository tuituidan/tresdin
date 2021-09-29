package com.tuituidan.tresdin.dict.bean;

import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DictTree.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/30
 */
@Getter
@Setter
@Accessors(chain = true)
public class DictTree {

    private String id;

    private String name;

    private String valid;

    private List<DictInfo> children;

}
