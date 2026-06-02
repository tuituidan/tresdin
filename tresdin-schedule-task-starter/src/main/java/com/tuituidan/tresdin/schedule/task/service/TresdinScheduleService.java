package com.tuituidan.tresdin.schedule.task.service;

import com.tuituidan.tresdin.schedule.task.annotation.TaskName;
import com.tuituidan.tresdin.schedule.task.bean.ScheduleTask;
import com.tuituidan.tresdin.schedule.task.bean.ScheduleTaskLog;
import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import com.tuituidan.tresdin.schedule.task.consts.TaskEventEnum;
import com.tuituidan.tresdin.schedule.task.util.ScheduleLogUtils;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * TresdinScheduleService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@Aspect
@Service
@Slf4j
public class TresdinScheduleService implements SchedulingConfigurer, ApplicationRunner {

    @Resource
    private IScheduleTaskStorage scheduleTaskStorage;

    @Resource
    private IScheduleTaskLogStorage scheduleTaskLogStorage;

    @Resource
    private IScheduleTaskEventHandler scheduleTaskEventHandler;

    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

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
    private List<ScheduleTask> taskItemList;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<CronTask> cronTaskList = taskRegistrar.getCronTaskList();
        taskItemList = new ArrayList<>(cronTaskList.size());
        for (CronTask cronTask : cronTaskList) {
            Method method = ((ScheduledMethodRunnable) cronTask.getRunnable()).getMethod();
            Class<?> declaringClass = method.getDeclaringClass();
            String taskFullPath = declaringClass.getName() + "." + method.getName();
            String taskId = DigestUtils.md5DigestAsHex(taskFullPath.getBytes(StandardCharsets.UTF_8));
            String taskPath = declaringClass.getSimpleName() + "." + method.getName();
            String taskName = Optional.ofNullable(AnnotationUtils.findAnnotation(method, TaskName.class))
                    .map(TaskName::value).orElse(taskPath);
            ScheduleTask taskItem = new ScheduleTask()
                    .setId(taskId)
                    .setTaskName(taskName)
                    .setTaskFullPath(taskFullPath)
                    .setTaskPath(taskPath)
                    .setCronTask(cronTask)
                    .setStatus(JobStatus.STOP);
            taskItemList.add(taskItem);
        }
        taskItemList.sort(Comparator.comparing(ScheduleTask::getTaskPath));
        taskRegistrar.setCronTasksList(Collections.emptyList());
    }

    /**
     * 列表.
     *
     * @return List
     */
    public List<ScheduleTask> getTaskItemList() {
        return taskItemList;
    }

    /**
     * 开启.
     *
     * @param taskId String
     * @return boolean
     */
    public boolean start(String taskId) {
        ScheduleTask taskItem = getTaskItem(taskId);
        if (isScheduleCancelled(taskItem)) {
            taskItem.setScheduledFuture(scheduler.schedule(
                            taskItem.getCronTask().getRunnable(),
                            taskItem.getCronTask().getTrigger()))
                    .setStatus(JobStatus.WAITING_NEXT);
            log.info("任务：{} ,已启动", taskItem.getTaskName());
            scheduleTaskStorage.saveTask(taskItem.getTaskFullPath(), JobStatus.WAITING_NEXT);
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
    private boolean isScheduleCancelled(ScheduleTask taskItem) {
        return taskItem.getCronTask() != null
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
        ScheduleTask taskItem = getTaskItem(taskId);
        if (taskItem.getScheduledFuture() != null
                && !taskItem.getScheduledFuture().isCancelled()) {
            taskItem.getScheduledFuture().cancel(false);
            taskItem.setScheduledFuture(null).setStatus(JobStatus.STOP);
            log.info("任务：{} ,已关闭", taskItem.getTaskName());
            scheduleTaskStorage.saveTask(taskItem.getTaskFullPath(), JobStatus.STOP);
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
     * execute
     *
     * @param taskId taskId
     */
    public void execute(String taskId) {
        getTaskItem(taskId).getCronTask().getRunnable().run();
    }

    /**
     * taskAround.
     *
     * @param joinPoint joinPoint
     * @throws Throwable ex
     */
    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object taskAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Signature signature = joinPoint.getSignature();
        String taskId = DigestUtils.md5DigestAsHex((targetClass.getName()
                + "." + signature.getName()).getBytes(StandardCharsets.UTF_8));
        ScheduleTaskLog taskLog = new ScheduleTaskLog()
                .setTaskId(taskId)
                .setTaskPath(targetClass.getSimpleName() + "." + signature.getName())
                .setStartTimeStamp(System.currentTimeMillis());
        ScheduleLogUtils.startLog(taskLog);
        scheduleTaskLogStorage.insertTaskLog(taskLog);
        ScheduleTask task = getTaskItem(taskId);
        eventHandler(TaskEventEnum.BEFORE, task, taskLog);
        try {
            Object proceed = joinPoint.proceed();
            eventHandler(TaskEventEnum.SUCCESS, task, taskLog);
            return proceed;
        } catch (Exception ex) {
            log.error("定时任务: {} 执行异常", taskLog.getTaskPath(), ex);
            taskLog.setSuccess(false).setMsg(StringUtils.truncate(ex.getMessage(), 500));
            eventHandler(TaskEventEnum.ERROR, task, taskLog);
            return null;
        } finally {
            if (taskLog.getSuccess() == null) {
                taskLog.setSuccess(true).setMsg("任务执行成功");
            }
            taskLog.setEndTimeStamp(System.currentTimeMillis());
            ScheduleLogUtils.endLog();
            scheduleTaskLogStorage.updateTaskLog(taskLog);
            eventHandler(TaskEventEnum.FINALLY, task, taskLog);
        }
    }

    private void eventHandler(TaskEventEnum event, ScheduleTask task, ScheduleTaskLog taskLog) {
        try {
            scheduleTaskEventHandler.eventHandler(event, task, taskLog);
        } catch (Exception ex) {
            log.error("任务回调事件处理异常", ex);
        }
    }

    private ScheduleTask getTaskItem(String taskId) {
        return taskItemList.stream().filter(it -> StringUtils.equals(taskId, it.getId()))
                .findFirst().orElseThrow(() -> new NullPointerException("任务不存在"));
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, JobStatus> existTaskMap = scheduleTaskStorage.selectTaskList().stream()
                .collect(Collectors.toMap(ScheduleTask::getTaskFullPath, ScheduleTask::getStatus));
        Set<String> existTaskKeys = existTaskMap.keySet();
        for (ScheduleTask task : taskItemList) {
            JobStatus jobStatus = existTaskMap.getOrDefault(task.getTaskFullPath(), task.getStatus());
            if (!JobStatus.STOP.equals(jobStatus)) {
                start(task.getId());
            }
            existTaskKeys.remove(task.getTaskFullPath());
        }
        if (CollectionUtils.isNotEmpty(existTaskKeys)) {
            scheduleTaskStorage.deleteTask(existTaskKeys);
        }
    }

}
