package com.tuituidan.tresdin.datatranslate.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * DictType.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface DictType {
    String value();
}
