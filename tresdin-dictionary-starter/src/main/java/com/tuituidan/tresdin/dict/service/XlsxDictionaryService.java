package com.tuituidan.tresdin.dict.service;

import com.alibaba.excel.EasyExcelFactory;
import com.tuituidan.tresdin.dict.bean.XlsxDict;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
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
    public List<DictInfo> loadDictList() {
        List<XlsxDict> result = new ArrayList<>();
        try {
            InputStream commonStream = this.getClass().getClassLoader()
                    .getResourceAsStream(DICT_COMMON_PATH);
            result.addAll(readXlsxToDictList(commonStream));
            Resource[] resources = ResourcePatternUtils
                    .getResourcePatternResolver(new PathMatchingResourcePatternResolver())
                    .getResources(DICT_SOURCE_PATH);
            for (Resource resource : resources) {
                result.addAll(readXlsxToDictList(resource.getInputStream()));
            }
            return BeanExtUtils.copyList(result, DictInfo.class);
        } catch (IOException e) {
            throw new ResourceAccessException("数据字典文件加载失败，请检查classpath:dict-source下是否有数据字典配置文件", e);
        }
    }

    private List<XlsxDict> readXlsxToDictList(InputStream inputStream) {
        return EasyExcelFactory.read(inputStream)
                .autoCloseStream(true).head(XlsxDict.class).doReadAllSync()
                .stream().map(XlsxDict.class::cast)
                .filter(item -> StringUtils.isNotBlank(item.getPid()))
                .collect(Collectors.toList());
    }

}
