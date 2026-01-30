package com.tuituidan.tresdin.datatranslate.translator;

import com.tuituidan.tresdin.datatranslate.bean.TranslationParameter;

/**
 * 自定义翻译，需要子类指定翻译注解、实现获取翻译方法.
 *
 * @param <T> T
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
public interface ITranslator<T> {

    /**
     * 得到要显示的字段名
     *
     * @param fieldName 原字段名
     * @return String
     */
    default String getFieldName(String fieldName) {
        return fieldName + "Text";
    }

    /**
     * 获取翻译内容.
     *
     * @param translationParameter 翻译用到的参数.
     * @return 翻译值
     */
    Object translate(TranslationParameter translationParameter);

}
