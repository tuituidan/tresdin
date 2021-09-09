package com.tuituidan.tresdin.mybatis.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * IEntity.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/12/11
 * @param <T> T
 */
public interface IEntity<T> extends Serializable {

    /**
     * getBh.
     *
     * @return String
     */
    String getBh();

    /**
     * setBh.
     *
     * @param bh bh
     */
    T setBh(String bh);

    /**
     * getCjsj/
     *
     * @return LocalDateTime
     */
    LocalDateTime getCjsj();

    /**
     * setCjsj.
     *
     * @param cjsj cjsj
     * @return T
     */
    T setCjsj(LocalDateTime cjsj);

    /**
     * getZhgxsj.
     *
     * @return LocalDateTime
     */
    LocalDateTime getZhgxsj();

    /**
     * setZhgxsj.
     *
     * @param zhgxsj zhgxsj
     * @return T
     */
    T setZhgxsj(LocalDateTime zhgxsj);
}
