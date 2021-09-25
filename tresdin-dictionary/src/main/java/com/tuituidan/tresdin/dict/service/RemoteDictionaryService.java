package com.tuituidan.tresdin.dict.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 配置从远端服务获取.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/25
 */
@ConditionalOnProperty(value = "dictionary.type", havingValue = "remote")
@Service
public class RemoteDictionaryService extends AbstractDictionaryService {

    /**
     * loadCache
     */
    @Override
    public void loadCache() {
        // todo 暂未实现
    }

}
