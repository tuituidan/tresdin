package com.tuituidan.tresdin.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * RequestUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/10/15
 */
@UtilityClass
public class RequestUtils {

    /**
     * 和IP有关的头
     */
    private static final String[] HEADERS_IP_RELATED = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP"
    };

    /**
     * 从request中获取clientId
     *
     * @return String
     */
    public static String getClientIp() {
        String ip = getClientIpFromRequest(getRequest());
        if (!isValidIp(ip)) {
            return "";
        }
        String[] ips = ip.split(",");
        for (String i : ips) {
            if (isValidIp(i)) {
                ip = i;
                break;
            }
        }
        return ip;
    }

    private static String getClientIpFromRequest(HttpServletRequest request) {
        for (String header : HEADERS_IP_RELATED) {
            String ip = request.getHeader(header);
            if (isValidIp(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    private static boolean isValidIp(String ip) {
        return StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 得到request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletRequest"));
    }

}
