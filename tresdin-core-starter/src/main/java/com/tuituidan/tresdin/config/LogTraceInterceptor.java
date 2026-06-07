package com.tuituidan.tresdin.config;

import com.tuituidan.tresdin.consts.TresdinConsts;
import com.tuituidan.tresdin.util.StringExtUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * LogTraceInterceptor.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/6/7
 */
@Component
public class LogTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        MDC.put(TresdinConsts.TRACE_ID, StringUtils.defaultIfBlank(request.getHeader(TresdinConsts.TRACE_ID),
                StringExtUtils.getUuid()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        MDC.remove(TresdinConsts.TRACE_ID);
    }

}
