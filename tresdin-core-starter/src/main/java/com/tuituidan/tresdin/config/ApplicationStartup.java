package com.tuituidan.tresdin.config;

import com.tuituidan.tresdin.util.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * ApplicationStartup.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 通过配置控制是否开启.
     */
    @Value("${swagger.show:true}")
    private boolean swaggerShow;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        String swagger = swaggerShow ? "/swagger-ui/index.html" : "";
        log.info(NetworkUtils.getLocalUrl(environment) + swagger);
    }

}
