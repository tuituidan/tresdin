package com.tuituidan.tresdin.schedule.task.bean;

import com.tuituidan.tresdin.schedule.task.consts.JobStatus;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.scheduling.config.CronTask;

/**
 * TaskItem.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class TaskItem {

    /**
     * id.
     */
    private String id;

    /**
     * 任务名称.
     */
    private String name;

    /**
     * cron表达式.
     */
    private String cron;

    /**
     * cron描述.
     */
    private String desc;

    /**
     * 下次执行时间.
     */
    private Date nextRunTime;

    /**
     * 任务状态.
     */
    private JobStatus status;

    /**
     * scheduledFuture.
     */
    private ScheduledFuture<?> scheduledFuture;

    /**
     * cronTask.
     */
    private CronTask cronTask;

}
