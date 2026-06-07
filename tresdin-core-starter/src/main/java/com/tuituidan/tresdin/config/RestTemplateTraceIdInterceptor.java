package com.tuituidan.tresdin.config;

import com.tuituidan.tresdin.consts.TresdinConsts;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * RestTemplateTraceIdInterceptor.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/6/7
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String traceId = MDC.get(TresdinConsts.TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            request.getHeaders().add(TresdinConsts.TRACE_ID, traceId);
        }
        return execution.execute(request, body);
    }

}
