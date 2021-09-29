package com.tuituidan.tresdin.config;

import java.io.File;
import java.time.Duration;
import javax.servlet.MultipartConfigElement;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/14
 */
@Configuration
public class AppConfig {

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
     * @param restTemplateBuilder 构造器
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
                .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT))
                .build();
    }

    /**
     * 明确指定上传文件的临时目录，避免Tomcat使用默认的tmp而被操作系统清理掉
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/temp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            Assert.isTrue(tmpFile.mkdirs(), "临时上传路径创建失败");
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }

}
