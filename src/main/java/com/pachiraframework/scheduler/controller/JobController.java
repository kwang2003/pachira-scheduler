package com.pachiraframework.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.pachiraframework.common.ExecuteResult;
import com.pachiraframework.domain.Page;
import com.pachiraframework.scheduler.dto.SearchJobCriteria;
import com.pachiraframework.scheduler.entity.Job;
import com.pachiraframework.scheduler.service.JobService;
import com.pachiraframework.web.controller.BaseController;

/**
 * @author kevin
 *
 */
@Controller
public class JobController extends BaseController{
	@Autowired
	private JobService jobService;
	public ResponseEntity<ExecuteResult<Page<Job>>> search(SearchJobCriteria criteria){
		Page<Job> page = jobService.search(criteria);
		ExecuteResult<Page<Job>> result = ExecuteResult.newSuccessResult(page);
		return ResponseEntity.ok(result);
	}
}
