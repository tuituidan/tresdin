package com.tuituidan.tresdin.schedule.task.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class TaskItemVo {

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextRunTime;

    /**
     * 任务状态.
     */
    private String status;

    /**
     * 任务状态
     */
    private String statusDesc;

}
