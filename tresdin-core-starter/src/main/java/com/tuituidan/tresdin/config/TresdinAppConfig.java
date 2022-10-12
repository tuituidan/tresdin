package com.tuituidan.tresdin.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/14
 */
@Configuration
public class TresdinAppConfig {

    /**
     * 连接超时时间.
     */
    private static final int CONNECT_TIMEOUT = 30;

    /**
     * 读取数据时间.
     */
    private static final int READ_TIMEOUT = 120;

    /**
     * 初始RestTemplate 设置超时时间.
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
                .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT))
                .build();
    }

}
