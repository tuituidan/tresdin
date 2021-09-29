package com.tuituidan.tresdin.dict.service;

import com.alibaba.excel.EasyExcelFactory;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.dict.bean.XlsxDict;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictType;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

/**
 * 配置从本地classpath下dict-source中加载.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/23
 */
@ConditionalOnProperty(value = "dictionary.type", havingValue = "xlsx")
@Service
@Slf4j
public class XlsxDictionaryService extends AbstractDictionaryService {

    private static final String DICT_SOURCE_PATH = "dict-source/*.xlsx";

    private static final String DICT_COMMON_PATH = "dict-source/common.xlsx";

    @Override
    public void loadCache() {
        try {
            InputStream commonStream = this.getClass().getClassLoader()
                    .getResourceAsStream(DICT_COMMON_PATH);
            transResource(commonStream);
            Resource[] resources = ResourcePatternUtils
                    .getResourcePatternResolver(new PathMatchingResourcePatternResolver())
                    .getResources(DICT_SOURCE_PATH);
            for (Resource resource : resources) {
                transResource(resource.getInputStream());
            }
        } catch (IOException e) {
            throw new ResourceAccessException("数据字典文件加载失败，请检查classpath:dict-source下是否有数据字典配置文件", e);
        }
    }

    private void transResource(InputStream inputStream) {
        List<XlsxDict> datas = EasyExcelFactory.read(inputStream)
                .autoCloseStream(true).head(XlsxDict.class).doReadAllSync()
                .stream().map(XlsxDict.class::cast).collect(Collectors.toList());
        String pid = "";
        List<DictInfo> dictList = new ArrayList<>();
        for (XlsxDict data : datas) {
            if (StringUtils.isBlank(data.getPid())) {
                // 保存上一个
                if (StringUtils.isNotBlank(pid)) {
                    dictListCache.put(pid, new ArrayList<>(dictList));
                    dictList.clear();
                }
                dictTypeCache.put(data.getId(), BeanExtUtils.convert(data, DictType.class));
                pid = data.getId();
                continue;
            }
            DictInfo dict = BeanExtUtils.convert(data, DictInfo.class);
            dictList.add(dict);
            dictInfoCache.put(pid + Separator.HYPHEN + data.getId(), dict);
        }
    }

}
