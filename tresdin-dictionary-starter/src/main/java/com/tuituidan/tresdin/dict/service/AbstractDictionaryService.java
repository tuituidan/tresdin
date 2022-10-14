package com.tuituidan.tresdin.dict.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.dictionary.IDictionaryService;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictTree;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public List<DictInfo> getDictList(String type) {
        return dictListCache.getIfPresent(type);
    }

    @Override
    public DictInfo getDict(String type, String code) {
        return dictInfoCache.getIfPresent(type + Separator.HYPHEN + code);
    }

    @Override
    public List<DictTree> getDictTreeByType(String type) {
        List<DictInfo> dictList = getDictList(type);
        if (CollectionUtils.isEmpty(dictList)) {
            return Collections.emptyList();
        }
        // 节点排序
        List<DictTree> sourceList = dictList.stream()
                .map(item -> BeanExtUtils.convert(item, DictTree::new))
                .sorted(Comparator.comparing(DictTree::getLevel))
                .collect(Collectors.toList());
        LinkedHashMap<String, DictTree> sortMap = new LinkedHashMap<>();
        for (DictTree item : sourceList) {
            sortMap.put(item.getLevel(), item);
        }
        List<DictTree> result = new ArrayList<>();
        for (Entry<String, DictTree> entry : sortMap.entrySet()) {
            // 没有包含【.】号就是父节点
            if (StringUtils.isBlank(entry.getKey())
                    || entry.getKey().split("\\.").length <= 1) {
                result.add(entry.getValue());
                continue;
            }
            DictTree parent = sortMap.get(StringUtils.substringBeforeLast(entry.getKey(), "."));
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(entry.getValue());
            } else {
                log.warn("配置错误，type:{},节点:{}，找不到父节点", type, entry.getKey());
            }
        }
        return result;
    }

    @Override
    public void reloadCache() {
        dictInfoCache.invalidateAll();
        dictListCache.invalidateAll();
        loadCache();
    }

    /**
     * loadDictList
     *
     * @return List
     */
    public abstract List<DictInfo> loadDictList();

    @Override
    public void run(ApplicationArguments args) {
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
