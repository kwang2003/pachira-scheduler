package com.pachiraframework.scheduler.dto;

import com.pachiraframework.scheduler.entity.Job.TypeEnum;

import lombok.Data;

/**
 * @author kevin
 *
 */
@Data
public class AddJob {
	private String name;
	private String cron;
	private String description;
	/**
	 * 任务类型
	 */
	private TypeEnum type;
	/**
	 * 注册中心地址
	 */
	private String registry;
}
