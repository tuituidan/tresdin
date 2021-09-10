package com.tuituidan.tresdin.config;

import com.tuituidan.tresdin.util.NetworkUtils;

import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        String port = environment.getProperty("local.server.port");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        log.info("http://{}:{}{}", NetworkUtils.getLocalIp(), port, contextPath);
    }
}
