package com.pachiraframework.scheduler.component.zookeeper;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangxuzheng
 *
 */
@Component
public class InstanceRemovedEventHandler extends AbstractZookeeperEventHandler {
	@Autowired
	private ZookeeperJobElector zookeeperJobElector;
	@Override
	protected boolean match(WatchedEvent event) {
		String path = event.getPath();
		EventType eventType = event.getType();
		if(EventType.NodeDeleted.equals(eventType) && path.startsWith(ZookeeperJobConstants.JOB_INSTANCES_PATH+"/")) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleInternal(WatchedEvent event) throws KeeperException, InterruptedException {
		String path = event.getPath();
		String instance = path.substring(path.lastIndexOf("/"));
		List<String> children = zooKeeper.getChildren(ZookeeperJobConstants.JOB_PATH, false);
		for(String child : children) {
			String node = ZookeeperJobConstants.JOB_PATH + "/" + child;
			byte[] data = zooKeeper.getData(node, null, new Stat());
			String leader = new String(data);
			if(leader.equals(instance)) {
				zookeeperJobElector.electLeader(Long.valueOf(child));
			}
		}
	}

}
