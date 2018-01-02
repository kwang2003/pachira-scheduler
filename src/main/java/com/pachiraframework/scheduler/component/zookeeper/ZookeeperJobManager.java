package com.pachiraframework.scheduler.component.zookeeper;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Iterator;
import java.util.UUID;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.pachiraframework.scheduler.dto.EditJob;
import com.pachiraframework.scheduler.entity.Job;

import lombok.Setter;
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
	@Setter
	@Autowired
	private ZooKeeper zooKeeper;
	/**
	 * job运行实例ID
	 */
	private String instance = UUID.randomUUID().toString();
	/**
	 * 添加一个job
	 * @param job
	 */
	@SneakyThrows
	public void add(Job job) {
		checkArgument(job.getId()!=null);
		String jobNodePath = JobZookeeperConstants.JOB_PATH+"/"+job.getId();
		createIfNotExist(jobNodePath, CreateMode.PERSISTENT);
	}
	
	
	/** 
	 * 判断当前任务实例是否是选举的主节点
	 * @param job
	 * @return
	 */
	@SneakyThrows
	public boolean isLeader(Job job) {
		String jobNodePath = JobZookeeperConstants.JOB_PATH+"/"+job.getId();
		byte[] data = zooKeeper.getData(jobNodePath, null, new Stat());
		String json = new String(data);
		Gson gson = new Gson();
		JobNodeData jobData = gson.fromJson(json, JobNodeData.class);
		return this.instance.equals(jobData.getLeader());
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
		Iterator<String> iterator = Splitter.on("/").split(JobZookeeperConstants.JOB_PARENT_PATH).iterator();
		StringBuffer buffer = new StringBuffer("/");
		while(iterator.hasNext()) {
			String path = iterator.next();
			if(Strings.isNullOrEmpty(path)) {
				continue;
			}
			buffer.append(path);
			createIfNotExist(buffer.toString(), CreateMode.PERSISTENT);
			buffer.append("/");
		}
		
		//创建instances节点
		createIfNotExist(JobZookeeperConstants.JOB_INSTANCES_PATH, CreateMode.PERSISTENT);
		createIfNotExist(JobZookeeperConstants.JOB_INSTANCES_PATH+"/"+this.instance, CreateMode.EPHEMERAL);
	}
	
	private void createIfNotExist(String path,CreateMode mode) throws KeeperException, InterruptedException {
		Stat stat = zooKeeper.exists(path, true);
		if(stat == null) {
			zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
			log.info("zookeeper节点 {}创建成功",path);
		}else {
			log.warn("节点{}已经存在，不重复创建",path);
		}
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
