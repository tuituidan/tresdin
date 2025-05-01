package com.tuituidan.tresdin.mybatis.crypto.mybatis;

import com.tuituidan.tresdin.mybatis.crypto.annotation.CryptoField;
import com.tuituidan.tresdin.mybatis.crypto.config.CryptoConfig;
import com.tuituidan.tresdin.mybatis.crypto.util.AesUtils;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.util.ReflectionUtils;

/**
 * ParameterHandlerInterceptor.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/11/18
 */
@Slf4j
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)})
public class ParameterHandlerInterceptor implements Interceptor {

    @Resource
    private CryptoConfig cryptoConfig;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof ParameterHandler)) {
            return invocation.proceed();
        }
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        Object parameterObject = parameterHandler.getParameterObject();
        if (Objects.isNull(parameterObject)) {
            return invocation.proceed();
        }
        encryptObject(parameterObject);
        return invocation.proceed();
    }

    private void encryptObject(Object source) {
        if (source == null) {
            return;
        }
        if (source instanceof Map) {
            for (Map.Entry<?, ?> param : ((Map<?, ?>) source).entrySet()) {
                Object value = param.getValue();
                if (value instanceof ArrayList) {
                    for (Object obj : (ArrayList<?>) value) {
                        encryptObject(obj);
                    }
                } else {
                    encryptObject(value);
                }
            }
            return;
        }
        if (source instanceof List<?>) {
            for (Object obj : (List<?>) source) {
                encryptObject(obj);
            }
            return;
        }
        encryptBean(source);
    }

    private void encryptBean(Object source) {
        Field[] fields = FieldUtils.getAllFields(source.getClass());
        for (Field field : fields) {
            if (!field.isAnnotationPresent(CryptoField.class)) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, source);
            ReflectionUtils.setField(field, source, encryptValue(value));
        }
    }

    private Object encryptValue(Object value) {
        if (value instanceof String) {
            return encryptString((String) value);
        }
        if (value instanceof String[]) {
            return Arrays.stream((String[]) value)
                    .map(this::encryptString).toArray(String[]::new);
        }
        if (value instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object inVal : (List<?>) value) {
                result.add(encryptValue(inVal));
            }
            return result;
        }
        return value;
    }

    private String encryptString(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        if (StringUtils.startsWith(value, cryptoConfig.getEncryptPrefix())) {
            return value;
        }
        return cryptoConfig.getEncryptPrefix() + AesUtils.encrypt(value, cryptoConfig.getEncryptKey());
    }

}
