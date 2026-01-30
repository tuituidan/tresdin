package com.tuituidan.tresdin.sensitive;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * SensitiveConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025-10-17
 */
@Configuration
@ConditionalOnProperty(prefix = "sensitive", name = "enabled", havingValue = "true")
public class SensitiveConfig {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 添加敏感信息过滤器
     */
    @PostConstruct
    public void addSensitive() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                    List<BeanPropertyWriter> beanProperties) {
                for (BeanPropertyWriter writer : beanProperties) {
                    Sensitive annotation = writer.getMember().getAnnotation(Sensitive.class);
                    if (annotation != null) {
                        writer.assignSerializer(new SensitiveSerializer(annotation.type()));
                    }
                }
                return beanProperties;
            }
        });
        objectMapper.registerModule(simpleModule);
    }
}
