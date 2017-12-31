package com.pachiraframework.scheduler.config;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.pachiraframework.scheduler.component.AbstractJobRunner;
import com.pachiraframework.scheduler.component.JobRunnerContext;
import com.pachiraframework.scheduler.component.JobRunnerFactory;
import com.pachiraframework.scheduler.dao.JobDao;
import com.pachiraframework.scheduler.entity.Job;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kevin
 *
 */
@Slf4j
@Configuration
public class JobConfig implements SchedulingConfigurer {
	@Autowired
	private JobDao jobDao;
	@Autowired
	private JobRunnerFactory jobRunnerFactory;
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        
        List<Job> jobs = jobDao.getAll();
        for(Job job : jobs) {
        	taskRegistrar.addCronTask(createCronTask(job));
        	log.info("Add job {} info task registrar",job);
        }
	}
	
	private CronTask createCronTask(Job job) {
		CronTrigger cronTrigger = new CronTrigger(job.getCron());
		CronTask task = new CronTask(new Runnable() {
			@Override
			public void run() {
				AbstractJobRunner jobRunner = jobRunnerFactory.getJobRunner(job);
				JobRunnerContext context = new JobRunnerContext(jobRunner);
				context.run(job);
			}
		}, cronTrigger);
		return task;
	}
	
    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(150);
    }
}
