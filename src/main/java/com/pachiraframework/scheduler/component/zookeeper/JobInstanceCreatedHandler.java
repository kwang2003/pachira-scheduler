package com.pachiraframework.scheduler.component.zookeeper;

import java.util.List;
import java.util.Random;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * JobZookeeperConstants.JOB_INSTANCES_PATH 下创建节点--启动了新的任务运行实例的时候使用
 * @author wangxuzheng
 *
 */
@Slf4j
@Component
public class JobInstanceCreatedHandler extends AbstractZookeeperEventHandler {
	@Override
	protected boolean match(WatchedEvent event) {
		String path = event.getPath();
		EventType eventType = event.getType();
		if(EventType.NodeCreated.equals(eventType) && path.startsWith(JobZookeeperConstants.JOB_INSTANCES_PATH+"/")) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleInternal(WatchedEvent event) throws KeeperException, InterruptedException {
		String jobPath = JobZookeeperConstants.JOB_PATH;
		List<String> children = zooKeeper.getChildren(jobPath, false);
		if(children == null) {
			log.warn("no child found of node {}",jobPath);
			return;
		}
		for(String jobId : children) {
			electLeader(JobZookeeperConstants.JOB_INSTANCES_PATH, jobId);
		}
	}
	
	private void electLeader(String path,String jobId) throws KeeperException, InterruptedException {
		List<String> children = zooKeeper.getChildren(path, false);
		int length = children.size();
		int index = new Random().nextInt(length);
		String leader = children.get(index);
		String jobNodePath = JobZookeeperConstants.JOB_PATH+"/"+jobId;
		zooKeeper.setData(jobNodePath, nodeData(leader), -1);
		
		//确保选举出来的leader是合法的
		String leaderPath = path + "/"+leader;
		Stat stat = zooKeeper.exists(leaderPath, false);
		if(stat == null) {
			electLeader(path,jobId);
		}
	}
	
	private byte[] nodeData(String leader) {
		return leader.getBytes();
	}
}
