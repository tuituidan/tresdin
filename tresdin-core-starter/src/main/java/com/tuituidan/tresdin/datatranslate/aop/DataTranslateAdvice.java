package com.tuituidan.tresdin.datatranslate.aop;

import com.tuituidan.tresdin.datatranslate.annotation.DataTranslate;
import com.tuituidan.tresdin.datatranslate.service.DataTranslateService;

import javax.annotation.Resource;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * DataTranslateAdvice.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@ControllerAdvice
public class DataTranslateAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private DataTranslateService dataTranslateService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 有翻译注解 && 是ajax restController
        return returnType.hasMethodAnnotation(DataTranslate.class)
                && (returnType.hasMethodAnnotation(ResponseBody.class)
                || returnType.getDeclaringClass().getAnnotation(RestController.class) != null);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        return body == null ? null : dataTranslateService.handleValue(body);
    }

}
