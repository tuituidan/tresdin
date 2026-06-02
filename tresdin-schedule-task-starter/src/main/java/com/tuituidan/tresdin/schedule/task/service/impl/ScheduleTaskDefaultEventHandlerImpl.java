package com.tuituidan.tresdin.schedule.task.service.impl;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import com.tuituidan.tresdin.schedule.task.consts.TaskEventEnum;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskEventHandler;

/**
 * ScheduleTaskDefaultEventHandlerImpl.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/6/2
 */
public class ScheduleTaskDefaultEventHandlerImpl implements IScheduleTaskEventHandler {

    @Override
    public void eventHandler(TaskEventEnum event, ScheduleTask task, ScheduleTaskLog taskLog) {
        // 无实现
    }

}
