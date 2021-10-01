package com.tuituidan.tresdin.dict.service;

import com.alibaba.fastjson.JSON;
import com.tuituidan.tresdin.dict.config.DictionaryConfig;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 配置从远端服务获取.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/25
 */
@ConditionalOnProperty(value = "dictionary.type", havingValue = "remote")
@Service
@Slf4j
public class RemoteDictionaryService extends AbstractDictionaryService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DictionaryConfig dictionaryConfig;

    /**
     * loadCache
     */
    @Override
    public List<DictInfo> loadDictList() {
        Assert.hasText(dictionaryConfig.getUrl(), "请配置数据字典远端地址");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(dictionaryConfig.getUrl(), String.class);
        return JSON.parseArray(responseEntity.getBody(), DictInfo.class);
    }

}
