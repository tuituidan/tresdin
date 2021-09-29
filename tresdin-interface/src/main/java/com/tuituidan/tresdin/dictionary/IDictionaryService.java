package com.tuituidan.tresdin.dictionary;

import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictType;
import java.util.List;

/**
 * IDictionaryService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
public interface IDictionaryService {

    /**
     * getDictInfoList
     *
     * @param type type
     * @return List
     */
    List<DictInfo> getDictInfoList(String type);

    /**
     * getDictType
     *
     * @param type type
     * @return IDictType
     */
    DictType getDictType(String type);

    /**
     * getDictInfo.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    DictInfo getDictInfo(String type, String code);

    /**
     * reloadCache
     */
    void reloadCache();
}
