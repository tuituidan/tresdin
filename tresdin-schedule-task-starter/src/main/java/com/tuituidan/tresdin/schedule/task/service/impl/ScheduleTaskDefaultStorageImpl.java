package com.tuituidan.tresdin.schedule.task.service.impl;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskStorage;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * ScheduleTaskStorageImpl.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
public class ScheduleTaskDefaultStorageImpl implements IScheduleTaskStorage {

    @Override
    public List<ScheduleTask> selectTaskList() {
        return Collections.emptyList();
    }

    @Override
    public void saveTask(String fullPath, JobStatus status) {
        // save task
    }

    @Override
    public void deleteTask(Set<String> fullPaths) {
        // delete task
    }

}
