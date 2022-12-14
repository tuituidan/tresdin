package com.tuituidan.tresdin.dictionary;

import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictTree;
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
    List<DictInfo> getDictList(String type);

    /**
     * getDictInfo.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    DictInfo getDict(String type, String code);

    /**
     * getDictType
     *
     * @param type type
     * @return List
     */
    List<DictTree> getDictTreeByType(String type);

    /**
     * reloadCache
     */
    void reloadCache();

}
