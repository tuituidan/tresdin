package com.tuituidan.tresdin.dictionary;

import com.tuituidan.tresdin.dictionary.bean.IDictInfo;

/**
 * IDictionaryService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
public interface IDictionaryService {

    /**
     * getDictInfo.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    IDictInfo getDictInfo(String type, String code);

}
