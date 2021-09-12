package com.tuituidan.tresdin.mybatis.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Component;

/**
 * TresdinMybatisPropertySourcePostProcessor.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/11
 */
@Component
public class TresdinMybatisPropertySourcePostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment environment = beanFactory.getBean(ConfigurableEnvironment.class);
        MutablePropertySources propertySources = environment.getPropertySources();

        EncodedResource encodedResource = new EncodedResource(new ClassPathResource("tresdin-mybatis.properties"),
                StandardCharsets.UTF_8);

        PropertySource<?> propertySource;
        try {
            propertySource = new DefaultPropertySourceFactory()
                    .createPropertySource("tresdin-mybatis", encodedResource);
        } catch (IOException e) {
            throw new IllegalStateException("配置加载失败", e);
        }
        propertySources.addLast(propertySource);
    }
}
