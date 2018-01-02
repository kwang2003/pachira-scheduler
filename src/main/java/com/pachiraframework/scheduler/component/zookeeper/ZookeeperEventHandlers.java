package com.pachiraframework.scheduler.component.zookeeper;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	private InstanceRemovedEventHandler instanceRemovedEventHandler;
	
	public void handle(WatchedEvent event) {
		for(AbstractZookeeperEventHandler handler : this.handlers) {
			handler.handle(event);
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		this.handlers.add(this.jobNodeCreatedHandler);
		this.handlers.add(this.instanceRemovedEventHandler);
	}
}
