package com.tuituidan.tresdin.schedule.task.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@Getter
@AllArgsConstructor
public enum JobStatus {
    /**
     * 执行中.
     */
    RUNNING("running", "执行中"),

    /**
     * 等待执行.
     */
    WAITING_NEXT("waiting_next", "等待执行"),

    /**
     * 停止.
     */
    STOP("stop", "已停止");

    private String code;

    private String name;
}
