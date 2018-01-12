package com.pachiraframework.scheduler.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ContextLifecycleScheduledTaskRegistrar;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pachiraframework.scheduler.component.AbstractJobRunner;
import com.pachiraframework.scheduler.component.JobRunnerContext;
import com.pachiraframework.scheduler.component.JobRunnerFactory;
import com.pachiraframework.scheduler.component.KeyedCronTask;
import com.pachiraframework.scheduler.component.zookeeper.ZookeeperJobManager;
import com.pachiraframework.scheduler.dao.JobDao;
import com.pachiraframework.scheduler.entity.Job;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangxuzheng
 *
 */
@Slf4j
@Component
@Configuration
public class JobScheduler implements InitializingBean {
	private Map<String, ScheduledTask> taskMap = Maps.newConcurrentMap();
	@Autowired
	private JobRunnerFactory jobRunnerFactory;
	@Autowired
	private JobDao jobDao;
	@Autowired
	private ZookeeperJobManager zookeeperJobManager;
	@Autowired
	private ContextLifecycleScheduledTaskRegistrar contextLifecycleScheduledTaskRegistrar;

	/**
	 * 创建一个新的job
	 * 
	 * @param job
	 * @return
	 */
	private CronTask createCronTask(Job job) {
		KeyedCronTask task = new KeyedCronTask(new Runnable() {
			@Override
			public void run() {
				AbstractJobRunner jobRunner = jobRunnerFactory.getJobRunner(job);
				JobRunnerContext context = new JobRunnerContext(jobRunner);
				context.run(job);
			}
		}, job.getCron(), String.valueOf(job.getId()));
		return task;
	}

	/**
	 * 添加并调度一个新的任务
	 * 
	 * @param job
	 * @return 添加成功，返回true,任务已经存在时，返回false
	 */
	public boolean addNewTask(Job job) {
		List<CronTask> list = contextLifecycleScheduledTaskRegistrar.getCronTaskList();
//		Predicate<CronTask> predicate = task -> ((KeyedCronTask)task).getKey().equals(String.valueOf(job.getId()));
		boolean exist = false;
		for (CronTask cronTask : list) {
			KeyedCronTask task = (KeyedCronTask) cronTask;
			String key = task.getKey();
			if (key.equals(String.valueOf(job.getId()))) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			// this.contextLifecycleScheduledTaskRegistrar.addCronTask(createCronTask(job));
			ScheduledTask scheduledTask = this.contextLifecycleScheduledTaskRegistrar.scheduleCronTask(createCronTask(job));
			taskMap.put(String.valueOf(job.getId()), scheduledTask);
			log.info("added task {} to scheduler.",job.getId());
		}

		return !exist;
	}
	
	/**
	 * 从调度器中删除job
	 * @param jobId
	 * @return
	 */
	public void removeTask(Long jobId) {
		String key = String.valueOf(jobId);
		ScheduledTask scheduledTask = taskMap.get(key);
		if(scheduledTask != null) {
			scheduledTask.cancel();
			taskMap.remove(key);
			log.info("removed task {} from scheduler.",jobId);
		}
	}

	@Bean
	public ContextLifecycleScheduledTaskRegistrar contextLifecycleScheduledTaskRegistrar() {
		ContextLifecycleScheduledTaskRegistrar contextLifecycleScheduledTaskRegistrar = new ContextLifecycleScheduledTaskRegistrar();
		return contextLifecycleScheduledTaskRegistrar;
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(1000);
		scheduler.setThreadFactory(new SimpleThreadFactory());
		scheduler.setThreadNamePrefix("定时任务-");
		return scheduler;
	}

	private static class SimpleThreadFactory implements ThreadFactory {
		private final AtomicInteger threadCount = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "定时任务执行线程 -" + threadCount.getAndIncrement());
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Set<String> set = Sets.newHashSet();
		List<Job> jobs = jobDao.getAll();
		for (Job job : jobs) {
			zookeeperJobManager.add(job);
//			addNewTask(job);
//			log.info("Add job {} info task registrar", job);
			set.add(String.valueOf(job.getId()));
		}

//		// 清理没用的job node
//		List<String> existedJobs = zookeeperHelper.existedJobNodes();
//		if (existedJobs != null) {
//			for (String jobId : existedJobs) {
//				if (!set.contains(jobId)) {
//					String jobPath = ZookeeperJobConstants.JOB_PATH + ZookeeperJobConstants.PATH_SPLITOR + jobId;
//					zookeeperHelper.delete(jobPath);
//					log.info("删除没有关联的zookeeper job节点:{}",jobPath);
//				}
//			}
//		}
	}
}
