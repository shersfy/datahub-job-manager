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
        ServicesFeignClient client = jobInfoService.getServicesFeignClient(JobType.valueOf(job.getJobType()));
        // 调用服务执行任务
        sendMsg(Level.INFO, "dispatch job parameters config ...");
        String text = client.callExecuteJob(job.getId(), log.getId(), job.getConfig());

        res = JSON.parseObject(text, Result.class);
        if(res==null || res.getCode()!=ResultCode.SUCESS) {
            String err = "dispatch job parameters config error: "+res==null?"":res.getMsg();
            sendMsg(Level.ERROR, err);
            throw new DispatchException(err);
        }
        
        sendMsg(Level.INFO, "dispatch job parameters config successful");

    }

    public class DispatchException extends DatahubException{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        public DispatchException() {}

        public DispatchException(String message) {
            super(message);
        }

    }

}
