package com.tuituidan.tresdin.mybatis.mapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * BaseExtMapper.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/8/31
 */
@RegisterMapper
public interface BaseExtMapper<T> extends Mapper<T>, InsertListMapper<T>, IdsMapper<T> {

}
