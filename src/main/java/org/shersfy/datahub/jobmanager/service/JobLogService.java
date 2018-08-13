package org.shersfy.datahub.jobmanager.service;


import org.shersfy.datahub.jobmanager.model.JobLog;

/**
 * 任务执行历史记录服务接口
 */
public interface JobLogService extends BaseService<JobLog, Long> {

    int deleteByJobId(Long jobId);
	
}
