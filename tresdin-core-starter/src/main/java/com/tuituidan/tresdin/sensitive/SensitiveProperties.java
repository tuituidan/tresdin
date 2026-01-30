package com.tuituidan.tresdin.sensitive;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SensitiveProperties.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/10/18
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sensitive")
public class SensitiveProperties {

    private Boolean enabled;

}
