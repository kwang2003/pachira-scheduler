package com.pachiraframework.scheduler.component.zookeeper;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ZookeeperJobConstants.JOB_INSTANCES_PATH 下 
 * @author wangxuzheng
 *
 */
@Component
public class InstanceCreatedEventHandler extends AbstractZookeeperEventHandler {
	@Autowired
	private ZookeeperJobElector zookeeperJobElector;
	@Override
	protected boolean match(WatchedEvent event) {
		String path = event.getPath();
		EventType eventType = event.getType();
		if(EventType.NodeCreated.equals(eventType) && path.startsWith(ZookeeperJobConstants.JOB_INSTANCES_PATH+"/")) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleInternal(WatchedEvent event) throws KeeperException, InterruptedException {
		List<String> children = zooKeeper.getChildren(ZookeeperJobConstants.JOB_PATH, true);
		for(String child : children) {
			zookeeperJobElector.electLeader(Long.valueOf(child));
		}
	}

}
