package com.tuituidan.tresdin.schedule.task.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.tresdin.schedule.task.util.DurationUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ScheduleTaskLog.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
@Getter
@Setter
@Accessors(chain = true)
public class ScheduleTaskLog {

    private String taskId;

    private String taskPath;

    private Long startTimeStamp;

    /**
     * startTime.
     *
     * @return LocalDateTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime getStartTime() {
        if (startTimeStamp == null) {
            return null;
        }
        return Instant.ofEpochMilli(startTimeStamp)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Long endTimeStamp;

    /**
     * startTime.
     *
     * @return LocalDateTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime getEndTime() {
        if (endTimeStamp == null) {
            return null;
        }
        return Instant.ofEpochMilli(endTimeStamp)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String msg;

    private Boolean success;

    /**
     * costTimeDesc.
     *
     * @return String
     */
    public String getCostTimeDesc() {
        if (endTimeStamp == null || startTimeStamp == null) {
            return null;
        }
        return DurationUtils.durationFormat(startTimeStamp, endTimeStamp);
    }

}
