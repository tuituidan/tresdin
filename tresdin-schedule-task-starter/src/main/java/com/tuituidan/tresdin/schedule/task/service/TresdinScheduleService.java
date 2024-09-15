package com.tuituidan.tresdin.schedule.task.service;

import com.tuituidan.tresdin.schedule.task.annotation.TaskName;
import com.tuituidan.tresdin.schedule.task.bean.TaskItem;
import com.tuituidan.tresdin.schedule.task.bean.TaskItemVo;
import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import com.tuituidan.tresdin.schedule.task.util.CronAnalyzeKits;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

/**
 * TresdinScheduleService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@Service
@Slf4j
public class TresdinScheduleService implements SchedulingConfigurer {

    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

    @Value("${tresdin.schedule.task.auto:true}")
    private Boolean taskAuto;

    @Value("${tresdin.schedule.task.pool-size:1}")
    private Integer poolSize;

    @PostConstruct
    private void initThreadPoolTaskScheduler() {
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("定时任务-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.initialize();
    }

    /**
     * 任务列表.
     */
    private List<TaskItem> taskItemList;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<CronTask> cronTaskList = taskRegistrar.getCronTaskList();
        taskItemList = new ArrayList<>(cronTaskList.size());
        cronTaskList.forEach(cronTask -> {
            ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) cronTask.getRunnable();
            String taskName = Optional.ofNullable(
                            AnnotationUtils.findAnnotation(methodRunnable.getMethod(), TaskName.class))
                    .map(TaskName::value).orElse(StringUtils.EMPTY);
            TaskItem taskItem = new TaskItem()
                    .setId(DigestUtils.md5Hex(taskName))
                    .setName(taskName)
                    .setDesc(CronAnalyzeKits.analyzeCron(cronTask.getExpression()))
                    .setCron(cronTask.getExpression())
                    .setStatus(JobStatus.STOP)
                    .setCronTask(cronTask);
            if (BooleanUtils.isTrue(taskAuto)) {
                taskItem.setScheduledFuture(scheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger()))
                        .setNextRunTime(cronTask.getTrigger().nextExecutionTime(new SimpleTriggerContext()))
                        .setStatus(JobStatus.WAITING_NEXT);
            }
            taskItemList.add(taskItem);
        });
        final Collator collator = Collator.getInstance(Locale.CHINA);
        taskItemList.sort(Comparator.comparing(o -> collator.getCollationKey(o.getName())));
        taskRegistrar.setCronTasksList(Collections.emptyList());
    }

    /**
     * 列表.
     *
     * @return List
     */
    public List<TaskItemVo> getTaskItemList() {
        List<TaskItemVo> resultList = new ArrayList<>();
        for (TaskItem task : taskItemList) {
            if (JobStatus.STOP.equals(task.getStatus())) {
                task.setNextRunTime(null);
            } else {
                task.setStatus(task.getScheduledFuture().getDelay(TimeUnit.SECONDS) > 0
                        ? JobStatus.WAITING_NEXT : JobStatus.RUNNING);
                task.setNextRunTime(task.getCronTask().getTrigger().nextExecutionTime(new SimpleTriggerContext()));
            }
            TaskItemVo item = BeanExtUtils.convert(task, TaskItemVo::new);
            item.setStatus(task.getStatus().getCode());
            item.setStatusDesc(task.getStatus().getName());
            resultList.add(item);
        }
        return resultList;
    }

    /**
     * 开启.
     *
     * @param taskId String
     * @return boolean
     */
    public boolean start(String taskId) {
        TaskItem taskItem = taskItemList.stream()
                .filter(item -> item.getId().equals(taskId))
                .findFirst().orElse(null);
        if (isScheduleCancelled(taskItem)) {
            taskItem.setScheduledFuture(scheduler.schedule(
                    taskItem.getCronTask().getRunnable(),
                    taskItem.getCronTask().getTrigger()));
            log.info("任务：{} ,已启动", taskItem.getName());
            taskItem.setStatus(JobStatus.WAITING_NEXT);
            taskItem.setNextRunTime(taskItem.getCronTask().getTrigger()
                    .nextExecutionTime(new SimpleTriggerContext()));
            return true;
        }
        return false;
    }

    /**
     * isScheduleCancelled.
     *
     * @param taskItem TaskItem
     * @return boolean
     */
    private boolean isScheduleCancelled(TaskItem taskItem) {
        return taskItem != null
                && taskItem.getCronTask() != null
                && (taskItem.getScheduledFuture() == null
                || (taskItem.getScheduledFuture() != null && taskItem.getScheduledFuture().isCancelled()));
    }

    /**
     * 关闭.
     *
     * @param taskId String
     * @return boolean
     */
    public boolean stop(String taskId) {
        TaskItem taskItem = taskItemList.stream()
                .filter(item -> item.getId().equals(taskId))
                .findFirst().orElse(null);

        if (taskItem != null
                && taskItem.getScheduledFuture() != null
                && !taskItem.getScheduledFuture().isCancelled()) {
            taskItem.getScheduledFuture().cancel(false);
            log.info("任务：{} ,已关闭", taskItem.getName());
            taskItem.setStatus(JobStatus.STOP);
            return true;
        }
        return false;
    }

    /**
     * 重启.
     *
     * @param taskId String
     * @return boolean
     */
    public boolean restart(String taskId) {
        return stop(taskId) && start(taskId);
    }

    /**
     * 判断是否没在运行.
     *
     * @param taskId 任务ID
     * @return 是否运行中
     */
    public boolean isStop(String taskId) {
        TaskItem taskItem = taskItemList.stream()
                .filter(item -> item.getId().equals(taskId))
                .findFirst().orElse(null);
        return !(taskItem != null
                && !JobStatus.STOP.equals(taskItem.getStatus())
                && taskItem.getScheduledFuture() != null
                && taskItem.getScheduledFuture().getDelay(TimeUnit.SECONDS) < 0);
    }

    /**
     * handler
     *
     * @param taskId taskId
     */
    public void handler(String taskId) {
        taskItemList.stream()
                .filter(item -> item.getId().equals(taskId))
                .findFirst().ifPresent(taskItem -> taskItem.getCronTask().getRunnable().run());
    }

}
