package com.tuituidan.tresdin.schedule.task.service;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import java.util.List;
import java.util.Set;

/**
 * IScheduleTaskStorage.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
public interface IScheduleTaskStorage {

    /**
     * selectScheduleTaskList.
     *
     * @return List
     */
    List<ScheduleTask> selectTaskList();

    /**
     * saveScheduleTask.
     *
     * @param fullPath fullPath
     * @param status status
     */
    void saveTask(String fullPath, JobStatus status);

    /**
     * deleteScheduleTask.
     *
     * @param fullPaths fullPaths
     */
    void deleteTask(Set<String> fullPaths);

}
