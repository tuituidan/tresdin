package com.tuituidan.tresdin.dictionary;

import com.tuituidan.tresdin.dictionary.bean.IDictInfo;
import com.tuituidan.tresdin.dictionary.bean.IDictType;
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
    List<IDictInfo> getDictInfoList(String type);

    /**
     * getDictType
     *
     * @param type type
     * @return IDictType
     */
    IDictType getDictType(String type);

    /**
     * getDictInfo.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    IDictInfo getDictInfo(String type, String code);

    /**
     * reloadCache
     */
    void reloadCache();
}
