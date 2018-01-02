package com.pachiraframework.scheduler.component.zookeeper;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pachiraframework.scheduler.dto.EditJob;
import com.pachiraframework.scheduler.entity.Job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于zookeeper实现的任务选举执行
 * @author wangxuzheng
 *
 */
@Slf4j
@Component
public class ZookeeperJobManager implements InitializingBean,DisposableBean{
	@Autowired
	private ZooKeeper zooKeeper;
	@Autowired
	private ZookeeperJobElector jobElector;
	@Autowired
	private ZookeeperHelper zookeeperHelper;
	/**
	 * 添加一个job
	 * @param job
	 */
	@SneakyThrows
	public void add(Job job) {
		checkArgument(job.getId()!=null);
		String jobNodePath = ZookeeperJobConstants.JOB_PATH+"/"+job.getId();
		zookeeperHelper.createPersistentNodeIfNotExist(jobNodePath);
		jobElector.electLeader(job.getId());
	}
	
	/**
	 * 删除指定ID的job
	 * @param id
	 */
	public void delete(Long id) {
		
	}
	
	/**
	 * 暂停运行job
	 * @param id
	 */
	public void pause(Long id) {
		
	}
	
	/**
	 * 编辑新的任务属性
	 * @param newJob
	 */
	public void edit(EditJob newJob) {
		
	}
	
	private void initZookeeperNodes() throws KeeperException, InterruptedException {
		zookeeperHelper.createPersistentNodeIfNotExist(ZookeeperJobConstants.JOB_PARENT_PATH);
		//创建instances节点
		zookeeperHelper.createPersistentNodeIfNotExist(ZookeeperJobConstants.JOB_INSTANCES_PATH);
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		initZookeeperNodes();
	}

	@Override
	public void destroy() throws Exception {
		this.zooKeeper.close();
		log.info("zookeeper客户端注销成功");
	}
}
