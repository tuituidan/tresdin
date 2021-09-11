package com.tuituidan.tresdin.logback;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import ch.qos.logback.core.PropertyDefinerBase;
import org.apache.commons.lang3.StringUtils;

/**
 * LogbackPortGetProperty.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/11
 */
public class LogbackPortGetProperty extends PropertyDefinerBase {

    private static final String HTTP_PROTOCOL = "HTTP/1.1";

    private static final String DEFAULT_VALUE = "port";

    @Override
    public String getPropertyValue() {
        return getTomcatPortValue();
    }

    private String getTomcatPortValue() {
        try {
            List<MBeanServer> serverList = MBeanServerFactory.findMBeanServer(null);
            for (MBeanServer server : serverList) {
                Set<ObjectName> names = server.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
                for (ObjectName oname : names) {
                    String pvalue = (String) server.getAttribute(oname, "protocol");
                    if (StringUtils.equals(HTTP_PROTOCOL, pvalue)) {
                        return Objects.toString(server.getAttribute(oname, "port"));
                    }
                }
            }
            return DEFAULT_VALUE;
        } catch (Exception e) {
            return DEFAULT_VALUE;
        }
    }
}
