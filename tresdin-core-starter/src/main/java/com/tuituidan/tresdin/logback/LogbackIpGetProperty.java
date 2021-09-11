package com.tuituidan.tresdin.logback;

import com.tuituidan.tresdin.util.NetworkUtils;

import ch.qos.logback.core.PropertyDefinerBase;
import org.apache.commons.lang3.StringUtils;

/**
 * LogIpGetProperty.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/11
 */
public class LogbackIpGetProperty extends PropertyDefinerBase {

    private static final String DEFAULT_VALUE = "ip";

    @Override
    public String getPropertyValue() {
        return StringUtils.defaultString(NetworkUtils.getLocalIp(), DEFAULT_VALUE);
    }
}
