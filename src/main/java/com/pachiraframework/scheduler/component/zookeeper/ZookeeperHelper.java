package com.pachiraframework.scheduler.component.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangxuzheng
 *
 */
@Slf4j
@Component
public class ZookeeperHelper {
	@Autowired
	private ZooKeeper zooKeeper;
	public void createPersistentNodeIfNotExist(String path) throws KeeperException, InterruptedException {
		create(path, CreateMode.PERSISTENT);
	}
	
	/**
	 * 删除zookeeper上的节点
	 * @param path
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 */
	public void delete(String path) throws InterruptedException, KeeperException {
		zooKeeper.delete(path, -1);
	}
	
	private void create(String givenPath,CreateMode createMode) throws KeeperException, InterruptedException {
		String[] paths = givenPath.split("/");
		int length = paths.length;
		StringBuffer buffer = new StringBuffer("/");
		for(int i = 0; i < length; i++) {
			String path = paths[i];
			if(Strings.isNullOrEmpty(path)) {
				continue;
			}
			buffer.append(path);
			Stat stat = zooKeeper.exists(buffer.toString(), true);
			if(stat == null) {
				CreateMode mode = (i == length - 1) ? createMode : CreateMode.PERSISTENT;
				zooKeeper.create(buffer.toString(), null, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
				log.info("zookeeper节点 {}创建成功",buffer.toString());
			}else {
				log.warn("节点{}已经存在，不重复创建",buffer.toString());
			}
			buffer.append("/");
		}
	}
	
	/**
	 * 创建临时节点
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void createEphemeralNodeIfNotExist(String path) throws KeeperException, InterruptedException {
		create(path, CreateMode.EPHEMERAL);
	}
	
}
