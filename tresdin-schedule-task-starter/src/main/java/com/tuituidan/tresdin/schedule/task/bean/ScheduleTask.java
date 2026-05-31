package com.tuituidan.tresdin.schedule.task.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import com.tuituidan.tresdin.schedule.task.util.CronAnalyzeKits;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.SimpleTriggerContext;

/**
 * ScheduleTask.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
@Getter
@Setter
@Accessors(chain = true)
public class ScheduleTask {

    /**
     * 任务id.
     */
    private String id;

    /**
     * 任务全路径.
     */
    private String taskFullPath;

    /**
     * 任务路径.
     */
    private String taskPath;

    /**
     * 任务名称.
     */
    private String taskName;

    /**
     * cronTask.
     */
    @JsonIgnore
    @Transient
    private CronTask cronTask;

    /**
     * scheduledFuture.
     */
    @JsonIgnore
    @Transient
    private ScheduledFuture<?> scheduledFuture;

    /**
     * cron表达式.
     */
    public String getCron() {
        return cronTask.getExpression();
    }

    /**
     * cron表达式描述.
     */
    public String getCronDesc() {
        return CronAnalyzeKits.analyzeCron(cronTask.getExpression());
    }

    /**
     * 下次执行时间.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getNextRunTime() {
        return scheduledFuture == null
                ? null : cronTask.getTrigger().nextExecutionTime(new SimpleTriggerContext());
    }

    /**
     * 任务状态.
     */
    private JobStatus status;

    /**
     * 任务状态.
     */
    public JobStatus getStatus() {
        if (scheduledFuture == null) {
            return status;
        }
        return scheduledFuture.getDelay(TimeUnit.SECONDS) > 0
                ? JobStatus.WAITING_NEXT : JobStatus.RUNNING;
    }

}
