package com.tuituidan.tresdin.sensitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sensitive.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025-10-17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 敏感类型
     *
     * @return SensitiveType
     */
    SensitiveType type();

}
