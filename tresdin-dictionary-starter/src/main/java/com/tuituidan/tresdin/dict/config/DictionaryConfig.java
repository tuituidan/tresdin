package com.tuituidan.tresdin.dict.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * DictionaryConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/25
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "dictionary")
public class DictionaryConfig {

    /**
     * 获取字典数据的类型
     */
    private DictTypeEnum type = DictTypeEnum.XLSX;

    /**
     * 配置为REMOTE则通过这个地址获取
     */
    private String url;

    /**
     * 查询数据字典的sql
     */
    private String sql = "SELECT id, pid, name, level, order, valid FROM db_dict.dict_info";

    enum DictTypeEnum {
        /**
         * XLSX, DATABASE, REMOTE
         */
        XLSX, DATABASE, REMOTE;
    }

}
