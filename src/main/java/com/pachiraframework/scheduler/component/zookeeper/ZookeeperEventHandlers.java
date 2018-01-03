package com.pachiraframework.scheduler.component.zookeeper;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * @author wangxuzheng
 *
 */
@Component
public class ZookeeperEventHandlers implements InitializingBean{
	private List<AbstractZookeeperEventHandler> handlers = Lists.newArrayList();
	@Autowired
	private JobNodeCreatedHandler jobNodeCreatedHandler;
	@Autowired
	private InstanceChangedEventHandler changedEventHandler;
	
	public void handle(WatchedEvent event) {
		for(AbstractZookeeperEventHandler handler : this.handlers) {
			handler.handle(event);
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		this.handlers.add(this.jobNodeCreatedHandler);
		this.handlers.add(this.changedEventHandler);
	}
}
