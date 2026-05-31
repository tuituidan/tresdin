package com.tuituidan.tresdin.schedule.task.util;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * ScheduleUtils.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
@UtilityClass
@Slf4j
public class ScheduleLogUtils {

    private static final ThreadLocal<ScheduleTaskLog> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置任务结果.
     *
     * @param msg msg
     */
    public static void setSuccessMsg(String msg) {
        setResult(true, msg);
    }

    /**
     * 设置任务结果.
     *
     * @param msg msg
     */
    public static void setFailMsg(String msg) {
        setResult(false, msg);
    }

    /**
     * 设置任务结果.
     *
     * @param msg msg
     */
    private static void setResult(Boolean success, String msg) {
        THREAD_LOCAL.get().setSuccess(success).setMsg(msg);
    }

    /**
     * 开始记录任务结果.
     */
    public static void startLog(ScheduleTaskLog taskLog) {
        THREAD_LOCAL.set(taskLog);
    }

    /**
     * 结束记录任务结果.
     */
    public static void endLog() {
        THREAD_LOCAL.remove();
    }

}
