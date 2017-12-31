package com.pachiraframework.scheduler.component;

import com.google.common.base.Throwables;
import com.pachiraframework.scheduler.entity.Job;

import lombok.extern.slf4j.Slf4j;

/**
 * Job运行器
 * @author kevin
 *
 */
@Slf4j
public abstract class AbstractJobRunner {
	/**
	 * 执行Job
	 * @param job
	 * @return
	 */
	public void run(Job job) {
		if(this.canRun(job)) {
			beforeRun(job);
			try {
				this.runInternel(job);
				afterRun(job);
			}catch(Exception e) {
				log.error("Exceptions occurs when execute job,id={},name={},cron={},exception:\n{}",job.getId(),job.getName(),job.getCron(),Throwables.getStackTraceAsString(e));
				afterThrows(job, e);
			}
		}
	}
	protected abstract void runInternel(Job job);
	protected void beforeRun(Job job) {
		log.debug("fefore run job {}",job.getId());
	}
	
	protected void afterRun(Job job) {
		log.debug("after run job {}",job.getId());
	}
	
	protected void afterThrows(Job job,Exception e) {
		log.debug("after throws run job {}",job.getId());
	}
	/**
	 * 当前job在当前节点上是否具有运行资格
	 * @param job
	 * @return
	 */
	protected boolean canRun(Job job) {
		return true;
	}
}
