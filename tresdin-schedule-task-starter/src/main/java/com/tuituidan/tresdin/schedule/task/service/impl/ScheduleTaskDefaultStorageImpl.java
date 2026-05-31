package com.tuituidan.tresdin.schedule.task.service.impl;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * ScheduleTaskStorageImpl.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
public class ScheduleTaskDefaultStorageImpl implements IScheduleTaskStorage {

    private final Map<String, CircularFifoQueue<ScheduleTaskLog>> taskLogMap = new HashMap<>();

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

    @Override
    public List<ScheduleTaskLog> selectTaskLogList(String taskId) {
        CircularFifoQueue<ScheduleTaskLog> resultQueue = taskLogMap.get(taskId);
        if (CollectionUtils.isEmpty(resultQueue)) {
            return Collections.emptyList();
        }
        List<ScheduleTaskLog> logList = new ArrayList<>(resultQueue);
        Collections.reverse(logList);
        return logList;
    }

    @Override
    public void insertTaskLog(ScheduleTaskLog log) {
        taskLogMap.computeIfAbsent(log.getTaskId(),
                k -> new CircularFifoQueue<>(20)).add(log);
    }

    @Override
    public void updateTaskLog(ScheduleTaskLog log) {
        // update task
    }

}
