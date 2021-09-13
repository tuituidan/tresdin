package com.tuituidan.tresdin.config;

import ch.qos.logback.core.PropertyDefinerBase;
import com.tuituidan.tresdin.util.NetworkUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * LogbackIpGetProperty.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/13
 */
public class LogbackIpGetProperty extends PropertyDefinerBase {

    /**
     * 默认值
     */
    private static final String DEFAULT_VALUE = "ip";

    @Override
    public String getPropertyValue() {
        return StringUtils.defaultString(NetworkUtils.getLocalIp(), DEFAULT_VALUE);
    }

}
