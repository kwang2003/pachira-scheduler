/*
Navicat MariaDB Data Transfer

Source Server         : localhost
Source Server Version : 100113
Source Host           : localhost:3306
Source Database       : scheduler

Target Server Type    : MariaDB
Target Server Version : 100113
File Encoding         : 65001

Date: 2018-01-01 16:50:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `cron` varchar(64) NOT NULL,
  `type` varchar(10) NOT NULL COMMENT '任务类型，DBUUO,CONSUL,HTTP,EUREKA',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `registry` varchar(64) DEFAULT NULL COMMENT '注册中心地址',
  `interface_name` varchar(256) NOT NULL COMMENT '接口名称，http接口时为完整url',
  `method` varchar(100) NOT NULL COMMENT '要执行的方法名',
  `timeout` int(20) NOT NULL COMMENT '执行超时时间，单位毫秒',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES ('1', '测试任务', '测试一下', '0/5 * * * * ?', 'HTTP', '2018-01-01 11:17:15', '2018-01-01 11:17:18', 'DUBBO_ZOOKEEPER_LOCALHOST', 'http://www.baidu.com', 'GET', '5000');

-- ----------------------------
-- Table structure for job_history
-- ----------------------------
DROP TABLE IF EXISTS `job_history`;
CREATE TABLE `job_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL COMMENT '任务主键',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `cron` varchar(30) NOT NULL COMMENT 'cron',
  `started_at` datetime NOT NULL COMMENT '开始时间',
  `ended_at` datetime DEFAULT NULL COMMENT '结束时间',
  `status` varchar(10) NOT NULL COMMENT '执行状态，SUCCESS：成功，FAIL：失败，ING：进行中',
  `message` varchar(255) DEFAULT NULL COMMENT '消息',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of job_history
-- ----------------------------

-- ----------------------------
-- Table structure for service_registry
-- ----------------------------
DROP TABLE IF EXISTS `service_registry`;
CREATE TABLE `service_registry` (
  `id` varchar(50) NOT NULL,
  `name` varchar(64) NOT NULL COMMENT '注册中心名称',
  `address` varchar(256) NOT NULL COMMENT '注册中心地址',
  `type` varchar(20) NOT NULL COMMENT '注册中心类型，DUBBO_ZOOKEEPER',
  `properties` varchar(255) DEFAULT '',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of service_registry
-- ----------------------------
INSERT INTO `service_registry` VALUES ('DUBBO_ZOOKEEPER_LOCALHOST', '本地环境zookeeper注册中心', 'zookeeper://localhost:2181', 'DUBBO_ZOOKEEPER', '', '2018-01-01 11:16:36', '2018-01-01 11:16:36');
