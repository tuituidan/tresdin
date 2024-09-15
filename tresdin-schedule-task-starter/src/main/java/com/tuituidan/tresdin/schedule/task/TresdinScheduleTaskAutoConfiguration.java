package com.tuituidan.tresdin.schedule.task;

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

}
