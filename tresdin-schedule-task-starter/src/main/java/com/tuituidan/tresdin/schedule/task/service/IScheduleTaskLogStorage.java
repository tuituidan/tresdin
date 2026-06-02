package com.tuituidan.tresdin.schedule.task.service;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import java.util.List;

/**
 * IScheduleTaskStorage.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
public interface IScheduleTaskLogStorage {

    /**
     * getScheduleTaskLogList.
     *
     * @param taskId taskId
     * @return List
     */
    List<ScheduleTaskLog> selectTaskLogList(String taskId);

    /**
     * insertScheduleTaskLog.
     *
     * @param log log
     */
    void insertTaskLog(ScheduleTaskLog log);

    /**
     * updateScheduleTaskLog.
     *
     * @param log log
     */
    void updateTaskLog(ScheduleTaskLog log);

}
