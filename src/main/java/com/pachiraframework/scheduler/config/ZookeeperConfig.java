package com.pachiraframework.scheduler.config;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pachiraframework.scheduler.component.zookeeper.ZookeeperEventHandlers;

import lombok.Setter;

/**
 * @author wangxuzheng
 *
 */
@Configuration
public class ZookeeperConfig {
	@Setter
	@Value("${zookeeper.address}")
	private String zkConnectionString;
	@Setter
	@Autowired
	private ZookeeperEventHandlers zookeeperEventHandlers;
	@Bean(destroyMethod="close")
	public ZooKeeper zookeeper() throws IOException {
		ZooKeeper zooKeeper = new ZooKeeper(this.zkConnectionString,5000,new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				zookeeperEventHandlers.handle(event);
			}
		});
		return zooKeeper;
	}
}
