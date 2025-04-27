package com.tuituidan.tresdin.mybatis.crypto.mybatis;

import com.tuituidan.tresdin.mybatis.crypto.annotation.CryptoField;
import com.tuituidan.tresdin.mybatis.crypto.config.CryptoConfig;
import com.tuituidan.tresdin.mybatis.crypto.util.AesUtils;
import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.util.ReflectionUtils;

/**
 * ResultSetHandlerInterceptor.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/11/18
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
@Slf4j
public class ResultSetHandlerInterceptor implements Interceptor {

    /**
     * 逗号.
     */
    private static final String COMMA = ",";

    @Resource
    private CryptoConfig cryptoConfig;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (Objects.isNull(result)) {
            return null;
        }
        if (!(result instanceof List<?>)) {
            return result;
        }
        for (Object source : (List<?>) result) {
            if (source instanceof String) {
                return decryptObject(result);
            }
            if (source instanceof Map) {
                Map<?, ?> mapData = (Map<?, ?>) source;
                MapUtils.putAll(mapData, mapData.entrySet().stream()
                        .map(entry -> new Object[] {entry.getKey(),
                                decryptObject(entry.getValue())}).toArray(Object[]::new));
            } else if (source != null) {
                decryptBean(source);
            }
        }
        return result;
    }

    private void decryptBean(Object source) {
        if (source == null) {
            return;
        }
        if (source instanceof List<?>) {
            for (Object obj : (List<?>) source) {
                decryptBean(obj);
            }
            return;
        }
        Field[] fields = FieldUtils.getAllFields(source.getClass());
        for (Field field : fields) {
            decryptField(field, source);
        }
    }

    private void decryptField(Field field, Object source) {
        if (!field.isAnnotationPresent(CryptoField.class)) {
            return;
        }
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, source);
        ReflectionUtils.setField(field, source, decryptObject(value));
    }

    private Object decryptObject(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof String) {
            return decryptString((String) value);
        }
        if (value instanceof String[]) {
            return Arrays.stream((String[]) value)
                    .map(this::decryptString).toArray(String[]::new);
        }
        if (value instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object inVal : (List<?>) value) {
                result.add(decryptObject(inVal));
            }
            return result;
        }
        return value;
    }

    private String decryptString(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        if (StringUtils.contains(value, COMMA)) {
            return Arrays.stream(StringUtils.split(value, COMMA))
                    .map(this::decryptString)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(COMMA));
        }
        if (StringUtils.startsWith(value, cryptoConfig.getEncryptPrefix())) {
            try {
                return AesUtils.decrypt(StringUtils.removeStart(value, cryptoConfig.getEncryptPrefix()),
                        cryptoConfig.getEncryptKey());
            } catch (Exception ex) {
                log.error("解密失败，密文:{}", value, ex);
                return StringUtils.EMPTY;
            }
        }
        return value;
    }

}
