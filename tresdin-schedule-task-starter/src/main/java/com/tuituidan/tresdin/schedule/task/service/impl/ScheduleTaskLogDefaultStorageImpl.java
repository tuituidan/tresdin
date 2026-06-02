package com.tuituidan.tresdin.schedule.task.service.impl;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskLogStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * ScheduleTaskStorageImpl.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
public class ScheduleTaskLogDefaultStorageImpl implements IScheduleTaskLogStorage {

    private final Map<String, CircularFifoQueue<ScheduleTaskLog>> taskLogMap = new HashMap<>();

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
