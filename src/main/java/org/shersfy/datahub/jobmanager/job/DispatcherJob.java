package org.shersfy.datahub.jobmanager.job;

import org.quartz.JobExecutionContext;
import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.commons.beans.Result.ResultCode;
import org.shersfy.datahub.commons.constant.JobConst.JobType;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.jobmanager.feign.ServicesFeignClient;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.JobLog;
import org.slf4j.event.Level;

import com.alibaba.fastjson.JSON;

/**
 * 调用其它服务组件API, 下发任务配置并执行任务
 * 
 * 2018年8月11日
 */
public class DispatcherJob extends BaseJob{

    @Override
    public void dispatch(JobExecutionContext context) throws DatahubException {
        
        JobInfo job = getJob();
        JobLog log  = getJobLog();
        
        Result res = null;
        try {
            ServicesFeignClient client = jobInfoService.getServicesFeignClient(JobType.valueOf(job.getJobType()));
            // 调用服务分发任务
            sendMsg2LogManager(Level.INFO, "dispatch job parameters config ...");
            String text = client.callConfigJob(job.getId(), log.getId(), job.getConfig());
            res = JSON.parseObject(text, Result.class);
            if(res.getCode()!=ResultCode.SUCESS) {
                sendMsg2LogManager(Level.ERROR, "dispatch job parameters config error: "+res.getMsg());
            } else {
                sendMsg2LogManager(Level.INFO, "dispatch job parameters config successful");
            }
            
        } finally {
            rollback(log, res);
        }
        
    }

    /**回滚删除执行记录**/
    private void rollback(JobLog log, Result res) {
        
        if(res==null||res.getCode()!=ResultCode.SUCESS) {
            int seconds = 60;
            while(seconds>0) {
                if(jobLogService.deleteById(log.getId())==1) {
                    LOGGER.info("delete job log, id={}", log.getId());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                seconds --;
            }
        }
    }
    
}
