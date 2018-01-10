package com.pachiraframework.scheduler.component.zookeeper;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pachiraframework.scheduler.config.JobScheduler;
import com.pachiraframework.scheduler.dao.JobDao;
import com.pachiraframework.scheduler.entity.Job;

import lombok.extern.slf4j.Slf4j;

/**
 * ZookeeperJobConstants.JOB_INSTANCES_PATH 下 节点发生增加
 * @author wangxuzheng
 *
 */
@Slf4j
@Component
public class JobInstanceAddedEventHandler extends AbstractZookeeperEventHandler {
	private static final String JOB_INSTANCE_NODE_REG = "^"+ZookeeperJobConstants.JOB_PATH+ZookeeperJobConstants.PATH_SPLITOR+"\\d{1,}"+ZookeeperJobConstants.PATH_SPLITOR + "\\S*";
	@Autowired
	private JobDao jobDao;
	@Autowired
	private JobScheduler jobScheduler;
	@Autowired
	private ZookeeperJobElector zookeeperJobElector;
	@Override
	protected boolean match(TreeCacheEvent event) {
		String path = event.getData().getPath();
		Type eventType = event.getType();
		if(Type.NODE_ADDED.equals(eventType) && path.matches(JOB_INSTANCE_NODE_REG)) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleInternal(TreeCacheEvent event) throws Exception {
		String path = event.getData().getPath();
		String jobId = path.substring(ZookeeperJobConstants.JOB_PATH.length() + 1, path.lastIndexOf(ZookeeperJobConstants.PATH_SPLITOR));
		Long id = Long.valueOf(jobId);
		zookeeperJobElector.electLeader(id);
		Job job = jobDao.getById(id);
		if(job == null) {
			log.warn("找不到job id={}的任务信息，无法加入到调度器中",jobId);
			return;
		}
		jobScheduler.addNewTask(job);
		log.warn("找到job id={}的任务信息，加入到任务调度器中（如果没有在调度器中）",jobId);
	}

}
