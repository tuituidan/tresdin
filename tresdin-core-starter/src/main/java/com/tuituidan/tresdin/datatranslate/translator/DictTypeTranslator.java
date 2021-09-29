package com.tuituidan.tresdin.datatranslate.translator;

import com.tuituidan.tresdin.datatranslate.annotation.DictType;
import com.tuituidan.tresdin.datatranslate.bean.TranslationParameter;
import com.tuituidan.tresdin.dictionary.IDictionaryService;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DictTypeTranslator.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@Component
@Slf4j
public class DictTypeTranslator implements ITranslator<DictType> {

    @Autowired(required = false)
    private IDictionaryService dictionaryService;

    /**
     * 获取翻译内容.
     *
     * @param translationParameter 翻译用到的参数.
     * @return 翻译值
     */
    @Override
    public String translate(TranslationParameter translationParameter) {
        if (dictionaryService == null) {
            log.warn("无法翻译，注解DictType依赖的IDictionaryService实例不存在！");
            return "";
        }
        DictType dictType = (DictType) translationParameter.getAnnotation();
        return Optional.ofNullable(translationParameter.getFieldValue())
                .map(Objects::toString)
                .map(code -> dictionaryService.getDictInfo(dictType.value(), code))
                .map(DictInfo::getName).orElse("");
    }
}
