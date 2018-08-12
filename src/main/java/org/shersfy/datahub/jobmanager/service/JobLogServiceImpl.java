package org.shersfy.datahub.jobmanager.service;

import javax.annotation.Resource;

import org.shersfy.datahub.jobmanager.mapper.BaseMapper;
import org.shersfy.datahub.jobmanager.mapper.JobLogMapper;
import org.shersfy.datahub.jobmanager.model.JobLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("jobLogService")
public class JobLogServiceImpl extends BaseServiceImpl<JobLog, Long> 
	implements JobLogService{
    
    @Resource
    private JobLogMapper mapper;

    @Override
    public BaseMapper<JobLog, Long> getMapper() {
        return mapper;
    }
    
}
