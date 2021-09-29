package com.tuituidan.tresdin.dict.service;

import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.dict.config.DictionaryConfig;
import com.tuituidan.tresdin.dictionary.bean.DictInfo;
import com.tuituidan.tresdin.dictionary.bean.DictType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
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
public class DatabaseDictionaryService extends AbstractDictionaryService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private DictionaryConfig dictionaryConfig;

    /**
     * loadCache
     */
    @Override
    public void loadCache() {
        List<DictType> dictTypes = jdbcTemplate.query(dictionaryConfig.getDictTypeSql(),
                new BeanPropertyRowMapper<>(DictType.class));
        List<DictInfo> dictInfos = jdbcTemplate.query(dictionaryConfig.getDictInfoSql(),
                new BeanPropertyRowMapper<>(DictInfo.class));
        dictTypeCache.putAll(dictTypes.stream().collect(Collectors.toMap(DictType::getId, Function.identity())));
        Map<String, List<DictInfo>> dictMap = dictInfos.stream().collect(Collectors.groupingBy(DictInfo::getPid));
        dictListCache.putAll(dictMap);
        for (Entry<String, List<DictInfo>> entry : dictMap.entrySet()) {
            for (DictInfo dictInfo : entry.getValue()) {
                dictInfoCache.put(entry.getKey() + Separator.HYPHEN + dictInfo.getId(), dictInfo);
            }
        }
    }

}
