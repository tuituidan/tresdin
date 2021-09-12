package com.tuituidan.tresdin.datatranslate.bean;

import java.lang.annotation.Annotation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 封装翻译用到的参数.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class TranslationParameter {
    /**
     * 翻译注解.
     */
    private Annotation annotation;

    /**
     * 字段值.
     */
    private Object fieldValue;

    /**
     * 翻译字段所在对象.
     */
    private Object obj;
}
