package com.tuituidan.tresdin.schedule.task.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TaskEventEnum.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/6/2
 */
@Getter
@AllArgsConstructor
public enum TaskEventEnum {
    /**
     * BEFORE.
     */
    BEFORE("任务开始"),
    /**
     * ERROR.
     */
    ERROR("任务异常"),
    /**
     * SUCCESS.
     */
    SUCCESS("任务成功"),
    /**
     * FINALLY.
     */
    FINALLY("任务结束");

    private final String desc;
}
