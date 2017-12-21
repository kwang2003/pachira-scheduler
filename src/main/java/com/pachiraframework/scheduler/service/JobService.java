package com.pachiraframework.scheduler.service;

import com.pachiraframework.domain.Page;
import com.pachiraframework.scheduler.dto.SearchJobCriteria;
import com.pachiraframework.scheduler.entity.Job;

/**
 * @author kevin
 *
 */
public interface JobService {
	public Page<Job> search(SearchJobCriteria criteria);
}
