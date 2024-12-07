package com.tuituidan.tresdin.mybatis.crypto;

import com.tuituidan.tresdin.mybatis.crypto.config.CryptoConfig;
import com.tuituidan.tresdin.mybatis.crypto.mybatis.ParameterHandlerInterceptor;
import com.tuituidan.tresdin.mybatis.crypto.mybatis.ResultSetHandlerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TresdinMybatisCryptoAutoConfiguration.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/11/18
 */
@Configuration
@ConditionalOnProperty(value = "mybatis.crypto.enabled", havingValue = "true")
public class TresdinMybatisCryptoAutoConfiguration {

    /**
     * cryptoConfig
     *
     * @return {@link CryptoConfig}
     */
    @Bean
    @ConfigurationProperties("mybatis.crypto")
    public CryptoConfig cryptoConfig() {
        return new CryptoConfig();
    }

    /**
     * 初始化.
     *
     * @return {@link ParameterHandlerInterceptor}
     */
    @Bean
    public ParameterHandlerInterceptor parameterHandlerInterceptor() {
        return new ParameterHandlerInterceptor();
    }

    /**
     * 初始化.
     *
     * @return {@link ResultSetHandlerInterceptor}
     */
    @Bean
    public ResultSetHandlerInterceptor resultSetHandlerInterceptor() {
        return new ResultSetHandlerInterceptor();
    }

}
