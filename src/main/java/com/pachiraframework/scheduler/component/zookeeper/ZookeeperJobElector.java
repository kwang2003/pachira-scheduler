package com.pachiraframework.scheduler.component.zookeeper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * job运行节点选举
 * @author wangxuzheng
 *
 */
@Slf4j
@Component
public class ZookeeperJobElector implements InitializingBean{
	/**
	 * job运行实例ID
	 */
//	private String instance = UUID.randomUUID().toString();
	private String instance = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	@Autowired
	private ZookeeperHelper zookeeperHelper;
	@Autowired
	private ZooKeeper zooKeeper;
	public void electLeader(Long jobId) throws KeeperException, InterruptedException {
		String path = ZookeeperJobConstants.JOB_INSTANCES_PATH;
		List<String> children = zooKeeper.getChildren(path, true);
		int length = children.size();
		int index = new Random().nextInt(length);
		String randomLeader = children.get(index);
		String jobNodePath = ZookeeperJobConstants.JOB_PATH+"/"+jobId;
		zooKeeper.setData(jobNodePath, nodeData(randomLeader), -1);
		
		//确保选举出来的leader是合法的
		String leaderPath = path + "/"+randomLeader;
		Stat stat = zooKeeper.exists(leaderPath, true);
		if(stat == null) {
			electLeader(jobId);
		}
		log.info("Job [{}],leader is {}",jobId,randomLeader);
	}
	private byte[] nodeData(String leader) {
		return leader.getBytes();
	}
	@SneakyThrows
	public String getJobLeader(Long jobId) {
		String path = ZookeeperJobConstants.JOB_PATH+"/"+jobId;
		byte[] data = zooKeeper.getData(path, true, new Stat());
		return new String(data);
	}
	
	/**
	 * 判断当前节点是否是运行指定job的主节点
	 * @param jobId
	 * @return
	 */
	public boolean isLeader(Long jobId) {
		String leader = this.getJobLeader(jobId);
		return this.instance.equals(leader);
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		zookeeperHelper.createEphemeralNodeIfNotExist(ZookeeperJobConstants.JOB_INSTANCES_PATH+"/"+this.instance);
	}
}
