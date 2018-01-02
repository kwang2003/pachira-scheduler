package com.pachiraframework.scheduler.component.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 
 * 创建job节点事件. JobZookeeperConstants.JOB_PATH
 * @author wangxuzheng
 *
 */
@Component
public class JobNodeCreatedHandler extends AbstractZookeeperEventHandler {
	@Autowired
	private ZookeeperJobElector jobElector;
	@Override
	protected boolean match(WatchedEvent event) {
		String path = event.getPath();
		EventType eventType = event.getType();
		if(EventType.NodeCreated.equals(eventType) && path.startsWith(ZookeeperJobConstants.JOB_PATH+"/")) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleInternal(WatchedEvent event) throws KeeperException, InterruptedException {
		String jobPath = event.getPath();
		String jobId = jobPath.substring(jobPath.lastIndexOf("/"));
		jobElector.electLeader(Long.valueOf(jobId));
	}
	
}
