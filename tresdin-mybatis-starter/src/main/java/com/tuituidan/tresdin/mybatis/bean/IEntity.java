package com.tuituidan.tresdin.mybatis.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * IEntity.
 *
 * @param <T> T
 * @author zhujunhan
 * @version 1.0
 * @date 2020/12/11
 */
public interface IEntity<T> extends Serializable {

    /**
     * getId.
     *
     * @return String
     */
    String getId();

    /**
     * setId.
     *
     * @param id id
     * @return T
     */
    T setId(String id);

    /**
     * getCreateTime.
     *
     * @return LocalDateTime
     */
    LocalDateTime getCreateTime();

    /**
     * setCreateTime.
     *
     * @param createTime createTime
     * @return T
     */
    T setCreateTime(LocalDateTime createTime);

    /**
     * getUpdateTime.
     *
     * @return LocalDateTime
     */
    LocalDateTime getUpdateTime();

    /**
     * setUpdateTime.
     *
     * @param updateTime updateTime
     * @return T
     */
    T setUpdateTime(LocalDateTime updateTime);
}
