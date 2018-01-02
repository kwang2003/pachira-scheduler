package com.pachiraframework.scheduler.component.zookeeper;

/**
 * @author wangxuzheng
 *
 */
public class JobZookeeperConstants {
	public static final String JOB_PARENT_PATH = "/pachira/job/default";
	public static final String JOB_PATH = JOB_PARENT_PATH + "/jobs";
	public static final String JOB_INSTANCES_PATH = JOB_PARENT_PATH + "/instances";
}
