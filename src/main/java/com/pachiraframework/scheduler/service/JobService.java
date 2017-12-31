package com.pachiraframework.scheduler.service;

import com.pachiraframework.common.ExecuteResult;
import com.pachiraframework.domain.Page;
import com.pachiraframework.scheduler.dto.AddJob;
import com.pachiraframework.scheduler.dto.EditJob;
import com.pachiraframework.scheduler.dto.SearchJobCriteria;
import com.pachiraframework.scheduler.entity.Job;

/**
 * @author kevin
 *
 */
public interface JobService {
	public Page<Job> search(SearchJobCriteria criteria);
	/**
	 * 添加一个新的job
	 * @param job
	 * @return
	 */
	public ExecuteResult<Job> add(AddJob job);
	public ExecuteResult<Job> edit(EditJob job);
	public ExecuteResult<Job> delete(Long jobId);
}
