package com.pachiraframework.scheduler.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pachiraframework.dao.BaseDao;
import com.pachiraframework.dao.support.SqlQueryCommand;
import com.pachiraframework.scheduler.entity.Job;

/**
 * @author kevin
 *
 */
@Repository
public class JobDao extends BaseDao {
	public List<Job> getAll(){
		SqlQueryCommand command = new SqlQueryCommand();
		return this.findListBySqlCommand(command);
//		List<Job> jobs = Lists.newArrayList();
//		Job job = new Job();
//		job.setCron("0 0 0 * * * ");
//		job.setId(1L);
//		job.setName("Hello Job");
//		job.setDescription("测试Job");
//		return jobs;
	}
}
