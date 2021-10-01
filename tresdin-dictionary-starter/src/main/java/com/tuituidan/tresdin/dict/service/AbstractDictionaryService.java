package com.tuituidan.tresdin.dict.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.dictionary.IDictionaryService;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
    protected Cache<String, DictInfo> dictInfoCache;

    @Resource
    protected Cache<String, List<DictInfo>> dictListCache;

    /**
     * getDictList
     *
     * @param type type
     * @return List
     */
    @Override
    public List<DictInfo> getDictList(String type) {
        return dictListCache.getIfPresent(type);
    }

    /**
     * getDict.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    @Override
    public DictInfo getDict(String type, String code) {
        return dictInfoCache.getIfPresent(type + Separator.HYPHEN + code);
    }

    /**
     * reloadCache
     */
    @Override
    public void reloadCache() {
        dictInfoCache.invalidateAll();
        dictListCache.invalidateAll();
        loadCache();
    }

    /**
     * loadDictList
     */
    public abstract List<DictInfo> loadDictList();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始加载数据字典.");
        long start = System.currentTimeMillis();
        loadCache();
        long end = System.currentTimeMillis();
        log.info("数据字典加载完成，耗时【{}】毫秒.", end - start);
    }

    private void loadCache() {
        List<DictInfo> dictInfos = loadDictList();
        if (CollectionUtils.isEmpty(dictInfos)) {
            log.warn("未能加载到任何字典数据！");
            return;
        }
        Map<String, List<DictInfo>> dictMap = dictInfos.stream().collect(Collectors.groupingBy(DictInfo::getPid));
        dictListCache.putAll(dictMap);
        for (Entry<String, List<DictInfo>> entry : dictMap.entrySet()) {
            for (DictInfo dictInfo : entry.getValue()) {
                dictInfoCache.put(entry.getKey() + Separator.HYPHEN + dictInfo.getId(), dictInfo);
            }
        }
    }

}
