package com.pachiraframework.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pachiraframework.job.LeaderElector;

/**
 * 任务执行节点选举
 * @author wangxuzheng
 *
 */
@Configuration
public class LeaderElectorConfig {
	@Value("${zookeeper.address}")
	private String zkConnectionString;
	@Bean
	public LeaderElector leaderElector(){
		LeaderElector elector = new LeaderElector();
		elector.setZkConnectionString(this.zkConnectionString);
		return elector;
	}
}
