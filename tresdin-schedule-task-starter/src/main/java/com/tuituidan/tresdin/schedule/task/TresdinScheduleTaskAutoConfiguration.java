package com.tuituidan.tresdin.schedule.task;

import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskEventHandler;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskLogStorage;
import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskStorage;
import com.tuituidan.tresdin.schedule.task.service.impl.ScheduleTaskDefaultEventHandlerImpl;
import com.tuituidan.tresdin.schedule.task.service.impl.ScheduleTaskDefaultStorageImpl;
import com.tuituidan.tresdin.schedule.task.service.impl.ScheduleTaskLogDefaultStorageImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TresdinScheduleTaskAutoConfiguration.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@Configuration
@EnableScheduling
@ComponentScan("com.tuituidan.tresdin.schedule.task")
public class TresdinScheduleTaskAutoConfiguration {

    /**
     * scheduleTaskStorage.
     *
     * @return IScheduleTaskStorage
     */
    @Bean
    @ConditionalOnMissingBean
    public IScheduleTaskStorage scheduleTaskStorage() {
        return new ScheduleTaskDefaultStorageImpl();
    }

    /**
     * scheduleTaskLogStorage.
     *
     * @return IScheduleTaskLogStorage
     */
    @Bean
    @ConditionalOnMissingBean
    public IScheduleTaskLogStorage scheduleTaskLogStorage() {
        return new ScheduleTaskLogDefaultStorageImpl();
    }

    /**
     * scheduleTaskEventHandler.
     *
     * @return IScheduleTaskEventHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public IScheduleTaskEventHandler scheduleTaskEventHandler() {
        return new ScheduleTaskDefaultEventHandlerImpl();
    }
}
