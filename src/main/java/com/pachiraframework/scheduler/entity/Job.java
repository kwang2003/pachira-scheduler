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
	/**
	 * 任务类型
	 */
	private String type;
	/**
	 * 注册中心地址
	 */
	private String registry;
	
	/**
	 * 要执行的接口名称
	 */
	private String interfaceName;
	/**
	 * 要执行的方法名
	 */
	private String method;
	
	/**
	 * 超时时间，单位毫秒
	 */
	private Long timeout;
	
	public static enum TypeEnum{
		DUBBO,CONSUL,EUREKA,HTTP;
	}
}
