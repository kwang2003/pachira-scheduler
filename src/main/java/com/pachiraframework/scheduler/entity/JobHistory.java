package com.pachiraframework.scheduler.entity;

import java.util.Date;

import com.pachiraframework.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务执行历史记录
 * @author kevin
 *
 */
@Getter
@Setter
@ToString(callSuper=true)
public class JobHistory extends BaseEntity<Long> {
	private static final long serialVersionUID = 3492100091826780726L;
	private Job job;
	/**
	 * 任务开始执行时间
	 */
	private Date startedAt;
	/**
	 * 任务结束时间
	 */
	private Date endedAt;
	/**
	 * 任务执行的结果消息（可能是异常）
	 */
	private String message;
}
