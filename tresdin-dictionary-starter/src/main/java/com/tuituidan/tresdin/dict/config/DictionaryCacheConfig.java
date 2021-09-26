package com.tuituidan.tresdin.dict.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tuituidan.tresdin.dictionary.bean.IDictInfo;
import com.tuituidan.tresdin.dictionary.bean.IDictType;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DictionaryCacheConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/24
 */
@Configuration
public class DictionaryCacheConfig {

    /**
     * dictTypeCache
     *
     * @return Cache
     */
    @Bean
    public Cache<String, IDictType> dictTypeCache() {
        return Caffeine.newBuilder().build();
    }

    /**
     * dictListCache
     *
     * @return Cache
     */
    @Bean
    public Cache<String, List<IDictInfo>> dictListCache() {
        return Caffeine.newBuilder().build();
    }

    /**
     * dictInfoCache
     *
     * @return Cache
     */
    @Bean
    public Cache<String, IDictInfo> dictInfoCache() {
        return Caffeine.newBuilder().build();
    }

}
