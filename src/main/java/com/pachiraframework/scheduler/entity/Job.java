package com.pachiraframework.scheduler.entity;

import com.pachiraframework.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author kevin
 *
 */
@Getter
@Setter
@ToString(callSuper=true)
public class Job extends BaseEntity<Long>{
	private static final long serialVersionUID = -307678602473614354L;
	/**
	 * 任务名称
	 */
	private String name;
	/**
	 * 任务描述
	 */
	private String description;
	/**
	 * cron表达式
	 */
	private String cron;
}
