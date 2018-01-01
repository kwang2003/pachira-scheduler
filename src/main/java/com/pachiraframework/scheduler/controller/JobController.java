package com.pachiraframework.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pachiraframework.common.ExecuteResult;
import com.pachiraframework.domain.Page;
import com.pachiraframework.scheduler.dto.SearchJobCriteria;
import com.pachiraframework.scheduler.entity.Job;
import com.pachiraframework.scheduler.service.JobService;
import com.pachiraframework.web.controller.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author kevin
 *
 */
@RestController
@RequestMapping("/job/")
public class JobController extends BaseController {
	@Autowired
	private JobService jobService;

	@ApiOperation(value = "任务查询", notes = "根据条件，检索匹配的任务列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "分页的任务列表信息") })
	@RequestMapping(path = "/search", method = { RequestMethod.GET })
	public ResponseEntity<ExecuteResult<Page<Job>>> search(SearchJobCriteria criteria) {
		Page<Job> page = jobService.search(criteria);
		ExecuteResult<Page<Job>> result = ExecuteResult.newSuccessResult(page);
		return ResponseEntity.ok(result);
	}
}
