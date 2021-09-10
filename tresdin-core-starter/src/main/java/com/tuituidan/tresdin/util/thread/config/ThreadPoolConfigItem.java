package com.tuituidan.tresdin.util.thread.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ThreadPoolConfigItem.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@Getter
@Setter
@Accessors(chain = true)
public class ThreadPoolConfigItem {

    /**
     * 线程名前缀.
     */
    private String threadNamePrefix;

    /**
     * 核心线程数.
     */
    private Integer corePoolNum;

    /**
     * 最大线程数.
     */
    private Integer maxPoolNum;

    /**
     * 线程存活时间.
     */
    private Long keepAliveTime;

    /**
     * 队列大小.
     */
    private Integer queueSize;

}
