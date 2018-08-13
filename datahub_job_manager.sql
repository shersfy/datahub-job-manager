/*
Database Name:	datahub
MySQL Version:	5.7.17 MySQL Community Server (GPL) for Linux (x86_64)
Author: 		py
Release Date:	2017-03-01
SQL_MODE:		STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
CMD: 			mysql -u -p -h --default-character-set=utf8 < ../datahub-release.sql
*/

CREATE DATABASE datahub_job_manager DEFAULT CHARACTER SET utf8 ;
use datahub_job_manager;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for job_info
-- ----------------------------
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE `job_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '项目ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `job_type` int(3) NOT NULL COMMENT '任务类型',
  `job_name` varchar(255) NOT NULL COMMENT '任务名称',
  `job_code` varchar(255) NOT NULL COMMENT '任务唯一编码',
  `job_class` varchar(255) DEFAULT NULL COMMENT '处理类名',
  `period_type` int(3) NOT NULL COMMENT '0:立即执行1次，1:周期性(cron表达式)',
  `active_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '生效时间',
  `expire_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '失效时间',
  `start_delay` bigint(20) NOT NULL COMMENT '启动延迟',
  `cron_expression` varchar(255) NOT NULL COMMENT 'cron表达式',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '调度状态(0：正常等待，1：调度中(默认)，2：调度完成)',
  `disable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '启用禁用(0:启用(默认)，1：禁用)',
  `config` longtext COMMENT '配置参数',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_key` (`job_code`,`job_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='定时任务';

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '结果状态(0：执行中(默认)，1：执行成功，2：执行失败)',
  `path` varchar(255) COMMENT '日志存放路径',
  `config` longtext COMMENT '历史配置参数',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务执行历史记录';

-- ----------------------------
-- Quartz config
-- ----------------------------
-- Quartz config start
-- # In your Quartz properties file, you'll need to set 
-- # org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
-- # By: Ron Cordell - roncordell
-- # I didn't see this anywhere, so I thought I'd post it here. This is the script from Quartz to create the tables in a MySQL database, modified to use INNODB instead of MYISAM.

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS(
	SCHED_NAME VARCHAR(120) NOT NULL,
	JOB_NAME VARCHAR(200) NOT NULL,
	JOB_GROUP VARCHAR(200) NOT NULL,
	DESCRIPTION VARCHAR(250) NULL,
	JOB_CLASS_NAME VARCHAR(250) NOT NULL,
	IS_DURABLE VARCHAR(1) NOT NULL,
	IS_NONCONCURRENT VARCHAR(1) NOT NULL,
	IS_UPDATE_DATA VARCHAR(1) NOT NULL,
	REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
	JOB_DATA BLOB NULL,
	PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	TRIGGER_NAME VARCHAR(200) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	JOB_NAME VARCHAR(200) NOT NULL,
	JOB_GROUP VARCHAR(200) NOT NULL,
	DESCRIPTION VARCHAR(250) NULL,
	NEXT_FIRE_TIME BIGINT(13) NULL,
	PREV_FIRE_TIME BIGINT(13) NULL,
	PRIORITY INTEGER NULL,
	TRIGGER_STATE VARCHAR(16) NOT NULL,
	TRIGGER_TYPE VARCHAR(8) NOT NULL,
	START_TIME BIGINT(13) NOT NULL,
	END_TIME BIGINT(13) NULL,
	CALENDAR_NAME VARCHAR(200) NULL,
	MISFIRE_INSTR SMALLINT(2) NULL,
	JOB_DATA BLOB NULL,
	PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
	REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	TRIGGER_NAME VARCHAR(200) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	REPEAT_COUNT BIGINT(7) NOT NULL,
	REPEAT_INTERVAL BIGINT(12) NOT NULL,
	TIMES_TRIGGERED BIGINT(10) NOT NULL,
	PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	TRIGGER_NAME VARCHAR(200) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	CRON_EXPRESSION VARCHAR(120) NOT NULL,
	TIME_ZONE_ID VARCHAR(80),
	PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS (          
	SCHED_NAME VARCHAR(120) NOT NULL,
	TRIGGER_NAME VARCHAR(200) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	STR_PROP_1 VARCHAR(512) NULL,
	STR_PROP_2 VARCHAR(512) NULL,
	STR_PROP_3 VARCHAR(512) NULL,
	INT_PROP_1 INT NULL,
	INT_PROP_2 INT NULL,
	LONG_PROP_1 BIGINT NULL,
	LONG_PROP_2 BIGINT NULL,
	DEC_PROP_1 NUMERIC(13,4) NULL,
	DEC_PROP_2 NUMERIC(13,4) NULL,
	BOOL_PROP_1 VARCHAR(1) NULL,
	BOOL_PROP_2 VARCHAR(1) NULL,
	PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	TRIGGER_NAME VARCHAR(200) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	BLOB_DATA BLOB NULL,
	PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
	FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	CALENDAR_NAME VARCHAR(200) NOT NULL,
	CALENDAR BLOB NOT NULL,
	PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	ENTRY_ID VARCHAR(95) NOT NULL,
	TRIGGER_NAME VARCHAR(200) NOT NULL,
	TRIGGER_GROUP VARCHAR(200) NOT NULL,
	INSTANCE_NAME VARCHAR(200) NOT NULL,
	FIRED_TIME BIGINT(13) NOT NULL,
	SCHED_TIME BIGINT(13) NOT NULL,
	PRIORITY INTEGER NOT NULL,
	STATE VARCHAR(16) NOT NULL,
	JOB_NAME VARCHAR(200) NULL,
	JOB_GROUP VARCHAR(200) NULL,
	IS_NONCONCURRENT VARCHAR(1) NULL,
	REQUESTS_RECOVERY VARCHAR(1) NULL,
	PRIMARY KEY (SCHED_NAME,ENTRY_ID)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE (
	SCHED_NAME VARCHAR(120) NOT NULL,
	INSTANCE_NAME VARCHAR(200) NOT NULL,
	LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
	CHECKIN_INTERVAL BIGINT(13) NOT NULL,
	PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
) ENGINE=InnoDB;

CREATE TABLE QRTZ_LOCKS (
	SCHED_NAME VARCHAR(120) NOT NULL,
	LOCK_NAME VARCHAR(40) NOT NULL,
	PRIMARY KEY (SCHED_NAME,LOCK_NAME)
) ENGINE=InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
-- Quartz config end