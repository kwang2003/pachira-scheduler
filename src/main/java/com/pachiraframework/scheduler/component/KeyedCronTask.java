package com.pachiraframework.scheduler.component;

import org.springframework.scheduling.config.CronTask;

import lombok.Getter;

/**
 * 带有Key唯一标识的cron task
 * @author wangxuzheng
 *
 */
public class KeyedCronTask extends CronTask {
	@Getter
	private String key;
	public KeyedCronTask(Runnable runnable, String expression,String key) {
		super(runnable, expression);
		this.key = key;
	}

}
