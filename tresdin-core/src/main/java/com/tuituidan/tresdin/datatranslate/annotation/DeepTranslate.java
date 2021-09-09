package com.tuituidan.tresdin.datatranslate.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 当对象嵌套时，深度翻译.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface DeepTranslate {
}
