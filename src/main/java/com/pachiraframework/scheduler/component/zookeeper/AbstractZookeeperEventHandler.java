package com.pachiraframework.scheduler.component.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 订阅处理zookeeper事件
 * @author wangxuzheng
 *
 */
@Slf4j
public abstract class AbstractZookeeperEventHandler {
	@Setter
	@Autowired
	protected ZooKeeper zooKeeper;
	/**
	 * 事件是否匹配当前处理器
	 * @param event
	 * @return
	 */
	protected abstract boolean match(WatchedEvent event) ;
	
	/**
	 * 事件处理模版方法
	 * @param event
	 */
	@SneakyThrows
	public void handle(WatchedEvent event) {
		log.info("================path={},type={}======================",event.getPath(),event.getType());
		if(match(event)) {
			String path = event.getPath();
			EventType type = event.getType();
			KeeperState state = event.getState();
			log.info("事件匹配成功,path={},type={},state={},处理器={}",path,type,state,this.getClass().getName());
			handleInternal(event);
		}
	}
	
	/**
	 * 处理具体的事件
	 * @param event
	 */
	protected abstract void handleInternal(WatchedEvent event) throws KeeperException, InterruptedException;
}
