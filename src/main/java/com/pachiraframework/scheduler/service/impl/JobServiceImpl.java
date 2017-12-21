package com.pachiraframework.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pachiraframework.domain.Page;
import com.pachiraframework.domain.WrappedPageRequest;
import com.pachiraframework.scheduler.dao.JobDao;
import com.pachiraframework.scheduler.dto.SearchJobCriteria;
import com.pachiraframework.scheduler.entity.Job;
import com.pachiraframework.scheduler.service.JobService;

/**
 * @author kevin
 *
 */
@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private JobDao jobDao;

	@Override
	public Page<Job> search(SearchJobCriteria criteria) {
		WrappedPageRequest pageRequest = new WrappedPageRequest(criteria);
		return jobDao.findByPage(pageRequest);
	}

}
