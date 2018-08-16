package org.shersfy.datahub.jobmanager.service;

import java.util.Date;

import javax.annotation.Resource;

import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.commons.beans.ResultMsg;
import org.shersfy.datahub.commons.constant.JobConst.JobLogStatus;
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

    @Override
    public int deleteByJobId(Long jobId) {
        return mapper.deleteByJobId(jobId);
    }
    
    @Override
    public int updateById(JobLog entity) {
        if(entity.getEndTime()==null) {
            entity.setEndTime(new Date());
        }
        return super.updateById(entity);
    }
    

    @Override
    public Result updateLog(Long logId, int status) {
        Result res = new Result();
        if(logId == null) {
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0001E000001, "logId"));
            return res;
        }
        
        JobLogStatus updstatus = JobLogStatus.valueOf(status);
        if(updstatus == JobLogStatus.Dummy) {
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0001E000001, "status"));
            return res;
        }
        
        JobLog old = findById(logId);
        if(old == null) {
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobLog", logId));
            return res;
        }
        
        JobLogStatus oldstatus = JobLogStatus.valueOf(old.getStatus());
        // 不需要更新
        if(oldstatus == JobLogStatus.Failed
            || oldstatus == updstatus) {
            return res;
        }
        
        JobLog udp = new JobLog();
        udp.setId(logId);
        udp.setStatus(status);
        this.updateById(udp);
        
        res.setModel(updstatus);
        return res;
    }

    
}
