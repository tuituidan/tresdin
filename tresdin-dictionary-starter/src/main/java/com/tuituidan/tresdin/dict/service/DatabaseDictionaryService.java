package com.tuituidan.tresdin.dict.service;

import com.tuituidan.tresdin.dict.config.DictionaryConfig;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 从数据库中获取.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/25
 */
@ConditionalOnProperty(value = "dictionary.type", havingValue = "database")
@Service
@Slf4j
public class DatabaseDictionaryService extends AbstractDictionaryService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private DictionaryConfig dictionaryConfig;

    /**
     * loadCache
     */
    @Override
    public List<DictInfo> loadDictList() {
        return jdbcTemplate.query(dictionaryConfig.getSql(),
                new BeanPropertyRowMapper<>(DictInfo.class));
    }

}
