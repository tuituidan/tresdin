package com.tuituidan.tresdin.dict.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * DictSwaggerConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/10/1
 */
@Configuration
@EnableOpenApi
public class DictSwaggerConfig {

    /**
     * 通过配置控制是否开启.
     */
    @Value("${swagger.show:true}")
    private boolean swaggerShow;

    /**
     * Docket docket.
     *
     * @return the docket
     */
    @Bean
    public Docket dictDocket() {
        return new Docket(DocumentationType.OAS_30)
                .enable(swaggerShow)
                .groupName("tresdin-dictionary")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tuituidan.tresdin.dict.controller"))
                .paths(regex("/api/v1.*"))
                .build();
    }

    /**
     * ApiInfo.
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("tresdin-dictionary")
                .description("tresdin-dictionary api。")
                .contact(new Contact("推推蛋", "", "tuituidan@163.com"))
                .termsOfServiceUrl("https://gitee.com/tuituidan/tresdin")
                .version("1.0.0")
                .build();
    }
}
