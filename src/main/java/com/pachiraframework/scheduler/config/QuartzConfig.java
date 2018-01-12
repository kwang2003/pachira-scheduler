package com.pachiraframework.scheduler.config;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxuzheng
 *
 */
@Configuration
public class QuartzConfig {
	@Bean
	public SchedulerFactory schedulerFactory() {
		StdSchedulerFactory schedulerFactory = new StdSchedulerFactory(); 
		return schedulerFactory;
	}
}
