package com.tuituidan.tresdin.schedule.task.controller;

import com.tuituidan.tresdin.consts.TresdinConsts;
import com.tuituidan.tresdin.schedule.task.bean.TaskItemVo;
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
@RequestMapping(TresdinConsts.API_V1 + "/tresdin/schedule/task")
public class TresdinTaskManagerController {

    @Resource
    private TresdinScheduleService tresdinScheduleService;

    /**
     * selectTaskList
     *
     * @return List
     */
    @GetMapping("/list")
    public List<TaskItemVo> selectTaskList() {
        return tresdinScheduleService.getTaskItemList();
    }

    /**
     * isStop
     *
     * @param taskId taskId
     * @return boolean
     */
    @GetMapping("/{taskId}/actions/check_stop")
    public boolean isStop(@PathVariable String taskId) {
        return tresdinScheduleService.isStop(taskId);
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
     * handler
     *
     * @param taskId taskId
     */
    @PostMapping("/{taskId}/actions/handler")
    public void handler(@PathVariable String taskId) {
        tresdinScheduleService.handler(taskId);
    }

}
