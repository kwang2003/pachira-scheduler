package com.pachiraframework.scheduler.dto;

import lombok.Data;

/**
 * @author kevin
 *
 */
@Data
public class EditJob {
	private Long jobId;
	private String cron;
	private String name;
	private String description;
}
