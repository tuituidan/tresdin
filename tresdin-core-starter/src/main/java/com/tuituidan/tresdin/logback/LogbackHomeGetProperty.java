package com.tuituidan.tresdin.logback;

import java.io.File;

import ch.qos.logback.core.PropertyDefinerBase;
import org.apache.commons.lang3.StringUtils;

/**
 * LogbackHomeGetProperty.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/11
 */
public class LogbackHomeGetProperty extends PropertyDefinerBase {

    /**
     * jetty.home.
     */
    private static final String JETTY_HOME = "jetty.home";

    /**
     * catalina.home.
     */
    private static final String CATALINA_HOME = "catalina.home";

    /**
     * 日志路径.
     */
    private static final String HOME = "logs";

    @Override
    public String getPropertyValue() {
        String jettyPath = System.getProperty(JETTY_HOME);
        String catalinaPath = System.getProperty(CATALINA_HOME);
        String path = StringUtils.defaultString(jettyPath, catalinaPath);
        if (StringUtils.isBlank(path)) {
            return HOME;
        }
        File file = new File(path);
        if (file.exists()) {
            return file.getPath() + File.separator + HOME;
        }
        return HOME;
    }
}
