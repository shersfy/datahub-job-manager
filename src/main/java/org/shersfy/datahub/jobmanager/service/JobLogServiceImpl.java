package org.shersfy.datahub.jobmanager.service;

import org.shersfy.datahub.jobmanager.mapper.BaseMapper;
import org.shersfy.datahub.jobmanager.model.JobLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("jobLogService")
public class JobLogServiceImpl extends BaseServiceImpl<JobLog, Long> 
	implements JobLogService{

    @Override
    public BaseMapper<JobLog, Long> getMapper() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
