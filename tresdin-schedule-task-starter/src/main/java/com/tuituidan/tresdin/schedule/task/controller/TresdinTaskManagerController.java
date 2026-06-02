package com.tuituidan.tresdin.schedule.task.controller;

import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskLogStorage;
import com.tuituidan.tresdin.schedule.task.service.TresdinScheduleService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaskManagerController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@RestController
@RequestMapping("/api/v1/tresdin/schedule/task")
public class TresdinTaskManagerController {

    @Resource
    private TresdinScheduleService tresdinScheduleService;

    @Resource
    private IScheduleTaskLogStorage scheduleTaskLogStorage;

    /**
     * selectTaskList
     *
     * @return List
     */
    @GetMapping("/list")
    public List<ScheduleTask> selectTaskList() {
        return tresdinScheduleService.getTaskItemList();
    }

    /**
     * selectTaskLogList
     *
     * @return List
     */
    @GetMapping("/{taskId}/log/list")
    public List<ScheduleTaskLog> selectTaskLogList(@PathVariable String taskId) {
        return scheduleTaskLogStorage.selectTaskLogList(taskId);
    }

    /**
     * start
     *
     * @param taskId taskId
     * @return boolean
     */
    @PostMapping("/{taskId}/actions/start")
    public boolean start(@PathVariable String taskId) {
        return tresdinScheduleService.start(taskId);
    }

    /**
     * stop
     *
     * @param taskId taskId
     * @return boolean
     */
    @PostMapping("/{taskId}/actions/stop")
    public boolean stop(@PathVariable String taskId) {
        return tresdinScheduleService.stop(taskId);
    }

    /**
     * restart
     *
     * @param taskId taskId
     * @return boolean
     */
    @PostMapping("/{taskId}/actions/restart")
    public boolean restart(@PathVariable String taskId) {
        return tresdinScheduleService.restart(taskId);
    }

    /**
     * execute
     *
     * @param taskId taskId
     */
    @PostMapping("/{taskId}/actions/execute")
    public void execute(@PathVariable String taskId) {
        tresdinScheduleService.execute(taskId);
    }

}
