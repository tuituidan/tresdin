package com.tuituidan.tresdin.schedule.task.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TaskName.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskName {

    /**
     * 任务名称
     *
     * @return String
     */
    String value();

}
