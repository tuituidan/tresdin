package com.tuituidan.tresdin.config;

import java.time.Duration;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * AppConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/14
 */
@Configuration
public class TresdinAppConfig implements WebMvcConfigurer, ApplicationListener<ApplicationReadyEvent> {

    /**
     * 连接超时时间.
     */
    private static final int CONNECT_TIMEOUT = 30;

    /**
     * 读取数据时间.
     */
    private static final int READ_TIMEOUT = 120;

    @Resource
    private LogTraceInterceptor logTraceInterceptor;

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
                .additionalInterceptors(new RestTemplateTraceIdInterceptor())
                .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logTraceInterceptor).addPathPatterns("/**");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String port = StringUtils.rightPad(event.getApplicationContext()
                .getEnvironment().getProperty("local.server.port"), 5);
        System.out.println("\n"
                + "+---------------------------------+ \n"
                + "| 程序启动成功，启动端口：" + port + "       | \n"
                + "+---------------------------------+ \n");
    }

}
