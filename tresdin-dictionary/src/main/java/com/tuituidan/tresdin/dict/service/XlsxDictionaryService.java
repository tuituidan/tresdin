package com.tuituidan.tresdin.dict.service;

import com.alibaba.excel.EasyExcelFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.tuituidan.tresdin.dict.bean.DictInfo;
import com.tuituidan.tresdin.dict.bean.DictType;
import com.tuituidan.tresdin.dict.bean.XlsxDict;
import com.tuituidan.tresdin.dictionary.IDictionaryService;
import com.tuituidan.tresdin.dictionary.bean.IDictInfo;
import com.tuituidan.tresdin.dictionary.bean.IDictType;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

/**
 * XlsxDictionaryService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/23
 */
@Service
@Slf4j
public class XlsxDictionaryService implements IDictionaryService, ApplicationRunner {

    private static final String DICT_SOURCE_PATH = "dict-source/*.xlsx";

    private static final String DICT_COMMON_PATH = "dict-source/common.xlsx";

    @javax.annotation.Resource
    private Cache<String, IDictType> dictTypeCache;

    @javax.annotation.Resource
    private Cache<String, IDictInfo> dictInfoCache;

    @javax.annotation.Resource
    private Cache<String, List<IDictInfo>> dictListCache;

    /**
     * getDictInfo.
     *
     * @param type type
     * @param code code
     * @return IDictInfo
     */
    @Override
    public IDictInfo getDictInfo(String type, String code) {
        return dictInfoCache.getIfPresent(type + "-" + code);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始加载数据字典.");
        long start = System.currentTimeMillis();
        loadCache();
        long end = System.currentTimeMillis();
        log.info("数据字典加载完成，耗时【{}】毫秒.", end - start);
    }

    private void loadCache() {
        try {
            InputStream commonStream = this.getClass().getClassLoader()
                    .getResourceAsStream(DICT_COMMON_PATH);
            transResource("common", commonStream);
            Resource[] resources = ResourcePatternUtils
                    .getResourcePatternResolver(new PathMatchingResourcePatternResolver())
                    .getResources(DICT_SOURCE_PATH);
            for (Resource resource : resources) {
                transResource(FilenameUtils.getBaseName(resource.getFilename()), resource.getInputStream());
            }
        } catch (IOException e) {
            throw new ResourceAccessException("数据字典文件加载失败，请检查classpath:dict-source下是否有数据字典配置文件", e);
        }
    }

    private void transResource(String systemTag, InputStream inputStream) {
        List<XlsxDict> datas = EasyExcelFactory.read(inputStream)
                .autoCloseStream(true).head(XlsxDict.class).doReadAllSync()
                .stream().map(XlsxDict.class::cast).collect(Collectors.toList());

        String pid = "";
        List<IDictInfo> dictList = new ArrayList<>();
        for (XlsxDict data : datas) {
            if (StringUtils.isBlank(data.getPid())) {
                // 保存上一个
                if (StringUtils.isNotBlank(pid)) {
                    dictListCache.put(pid, new ArrayList<>(dictList));
                    dictList.clear();
                }
                dictTypeCache.put(data.getId(), BeanExtUtils.convert(data, DictType.class).setSystemTag(systemTag));
                pid = data.getId();
                continue;
            }
            DictInfo dict = BeanExtUtils.convert(data, DictInfo.class);
            dictList.add(dict);
            dictInfoCache.put(pid + "-" + data.getId(), dict);
        }
    }

}
