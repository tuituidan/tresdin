package com.tuituidan.tresdin.schedule.task.config;

import com.tuituidan.tresdin.schedule.task.service.IScheduleTaskStorage;
import com.tuituidan.tresdin.schedule.task.service.impl.ScheduleTaskDefaultStorageImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ScheduleTaskConfig.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026/5/30
 */
@Configuration
public class ScheduleTaskConfig {

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

}
