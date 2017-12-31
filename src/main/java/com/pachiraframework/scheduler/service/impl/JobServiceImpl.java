package com.pachiraframework.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pachiraframework.common.ExecuteResult;
import com.pachiraframework.domain.Page;
import com.pachiraframework.domain.WrappedPageRequest;
import com.pachiraframework.scheduler.dao.JobDao;
import com.pachiraframework.scheduler.dto.AddJob;
import com.pachiraframework.scheduler.dto.EditJob;
import com.pachiraframework.scheduler.dto.SearchJobCriteria;
import com.pachiraframework.scheduler.entity.Job;
import com.pachiraframework.scheduler.service.JobService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kevin
 *
 */
@Slf4j
@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private JobDao jobDao;

	@Override
	public Page<Job> search(SearchJobCriteria criteria) {
		WrappedPageRequest pageRequest = new WrappedPageRequest(criteria);
		return jobDao.findByPage(pageRequest);
	}

	@Override
	public ExecuteResult<Job> add(AddJob addJob) {
		Job job = new Job();
		job.setName(addJob.getName());
		job.setDescription(addJob.getDescription());
		job.setCron(addJob.getCron());
		jobDao.insert(job);
		log.info("Added new Job ,id={},name={},cron={}",job.getId(),job.getName(),job.getCron());
		return null;
	}

	@Override
	public ExecuteResult<Job> edit(EditJob job) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecuteResult<Job> delete(Long jobId) {
		// TODO Auto-generated method stub
		return null;
	}

}
