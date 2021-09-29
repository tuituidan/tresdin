package com.tuituidan.tresdin.dict.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.dictionary.IDictionaryService;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictType;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 数据字段服务抽象类.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/25
 */
@Slf4j
public abstract class AbstractDictionaryService implements IDictionaryService, ApplicationRunner {

    @Resource
    protected Cache<String, DictType> dictTypeCache;

    @Resource
    protected Cache<String, DictInfo> dictInfoCache;

    @Resource
    protected Cache<String, List<DictInfo>> dictListCache;

    /**
     * getDictInfoList
     *
     * @param type type
     * @return List
     */
    @Override
    public List<DictInfo> getDictInfoList(String type) {
        return dictListCache.getIfPresent(type);
    }

    /**
     * getDictType
     *
     * @param type type
     * @return IDictType
     */
    @Override
    public DictType getDictType(String type) {
        return dictTypeCache.getIfPresent(type);
    }

    /**
     * getDictInfo.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    @Override
    public DictInfo getDictInfo(String type, String code) {
        return dictInfoCache.getIfPresent(type + Separator.HYPHEN + code);
    }

    /**
     * reloadCache
     */
    @Override
    public void reloadCache() {
        dictInfoCache.invalidateAll();
        dictListCache.invalidateAll();
        dictTypeCache.invalidateAll();
        loadCache();
    }

    /**
     * loadCache
     */
    public abstract void loadCache();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始加载数据字典.");
        long start = System.currentTimeMillis();
        loadCache();
        long end = System.currentTimeMillis();
        log.info("数据字典加载完成，耗时【{}】毫秒.", end - start);
    }

}
