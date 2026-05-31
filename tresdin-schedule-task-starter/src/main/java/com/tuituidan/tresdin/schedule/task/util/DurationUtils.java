package com.tuituidan.tresdin.schedule.task.util;

import java.time.Duration;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * DurationUtils.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/31
 */
@Slf4j
@UtilityClass
public class DurationUtils {

    /**
     * 毫秒数转可读时间描述.
     *
     * @param startStamp startStamp
     * @param endStamp endStamp
     * @return String
     */
    public static String durationFormat(long startStamp, long endStamp) {
        if (endStamp <= startStamp) {
            return "0毫秒";
        }
        Duration duration = Duration.ofMillis(endStamp - startStamp);
        StringBuilder sb = new StringBuilder();
        long days = duration.toDays();
        if (days > 0) {
            sb.append(days).append("天");
        }
        long hours = duration.toHours() % 24;
        if (hours > 0) {
            sb.append(hours).append("时");
        }
        long minutes = duration.toMinutes() % 60;
        if (minutes > 0) {
            sb.append(minutes).append("分");
        }
        long seconds = duration.getSeconds() % 60;
        if (seconds > 0) {
            sb.append(seconds).append("秒");
        }
        long millisPart = duration.toMillis() % 1000;
        if (millisPart > 0) {
            sb.append(millisPart).append("毫秒");
        }
        return sb.toString();
    }

}
