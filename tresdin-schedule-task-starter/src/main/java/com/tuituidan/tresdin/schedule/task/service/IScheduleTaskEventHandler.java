package com.tuituidan.tresdin.schedule.task.service;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import com.tuituidan.tresdin.schedule.task.consts.TaskEventEnum;

/**
 * IScheduleTaskStorage.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
public interface IScheduleTaskEventHandler {

    /**
     * eventHandler.
     *
     * @param event TaskEventEnum
     * @param task ScheduleTask
     * @param taskLog ScheduleTaskLog
     */
    void eventHandler(TaskEventEnum event, ScheduleTask task, ScheduleTaskLog taskLog);

}
