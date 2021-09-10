package com.tuituidan.tresdin.util.thread.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ThreadPoolConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolConfig {

    /**
     * 通用配置.
     */
    private ThreadPoolConfigItem common;
}
