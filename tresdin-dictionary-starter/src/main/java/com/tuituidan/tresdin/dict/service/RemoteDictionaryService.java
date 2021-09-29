package com.tuituidan.tresdin.dict.service;

import com.alibaba.fastjson.JSON;
import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.dict.bean.DictTree;
import com.tuituidan.tresdin.dict.config.DictionaryConfig;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictType;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    public void loadCache() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(dictionaryConfig.getUrl(), String.class);
        List<DictTree> trees = JSON.parseArray(responseEntity.getBody(), DictTree.class);
        if (CollectionUtils.isEmpty(trees)) {
            log.error("未能获取数据字典");
            return;
        }
        for (DictTree type : trees) {
            dictTypeCache.put(type.getId(), BeanExtUtils.convert(type, DictType.class));
            dictListCache.put(type.getId(), type.getChildren());
            for (DictInfo dictInfo : type.getChildren()) {
                dictInfoCache.put(type.getId() + Separator.HYPHEN + dictInfo.getId(), dictInfo);
            }
        }
    }

}
