package org.shersfy.datahub.jobmanager.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.commons.meta.LogMeta;
import org.shersfy.datahub.commons.meta.MessageData;
import org.shersfy.datahub.jobmanager.constant.Const.JobLogStatus;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.JobLog;
import org.shersfy.datahub.jobmanager.service.JobInfoService;
import org.shersfy.datahub.jobmanager.service.JobLogService;
import org.shersfy.datahub.jobmanager.service.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * Job基类
 */
public abstract class BaseJob implements Job{

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    private JobInfo job;
    private JobLog log;
    
    private Long timeOut;
    private JobDataMap dataMap;
    
    protected LogManager logManager;
    
    protected JobLogService jobLogService;
    
    protected JobInfoService jobInfoService;
    

	public BaseJob(){
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			// job执行前调用
			beforeJob(context);
			// 执行job
			dispatch(context);
			// job执行后调用
			afterJob();

		} catch (Exception ex) {
			// job执行异常时调用
			exceptionJob(DatahubException.throwDatahubException("job execute error", ex));
		} finally {
		    finallyDo();
		}
	}


    public abstract void dispatch(JobExecutionContext context) throws DatahubException;
    
    /**发送日志到日志管理器**/
    public void sendMsg2LogManager(Level level, String msg) {
        msg = msg == null?"":msg;
        msg = String.format("jobId={}, logId={}, %s", job.getId(), log.getId(), msg);
        
        LogMeta meta = new LogMeta(level, msg);
        String data  = "{\"jobId\": %s, \"logId\": %s, \"content\": \"%s\"}";
        data = String.format(data, job.getId(), log.getId(), meta.getLine());
        
        logManager.sendMsg(new MessageData(data));
    }

	/**
	 * job执行前调用
	 * @param context
	 * @throws DatahubException 
	 */
	public void beforeJob(JobExecutionContext context) throws DatahubException{
	    LOGGER.info("begining ...");
	    dataMap = context.getJobDetail().getJobDataMap();
	    timeOut = dataMap.getLong("dispatchJobTimeoutSeconds");
	    
	    jobInfoService = (JobInfoService) dataMap.get(JobInfoService.class.getName());
	    Long jobId = dataMap.getLong("jobId");
	    job = jobInfoService.findById(jobId);
        job = job==null?(JobInfo) dataMap.get("job"):job;
        
        
        // 插入执行记录
        jobLogService = jobInfoService.getJobLogService();
        log = new JobLog();
        log.setJobId(job.getId());
        log.setStatus(JobLogStatus.Executing.index());
        log.setStartTime(new Date());
        log.setEndTime(log.getEndTime());
        
        jobLogService.insert(log);
        LOGGER.info("jobId={}, logId={}, insert job log record", job.getId(), log.getId());
	}

	/**
	 * job正常执行后调用
	 *
	 */
	public void afterJob() throws DatahubException{
	    sendMsg2LogManager(Level.INFO, "execute successful");
	}
	
	private void finallyDo() {
	    sendMsg2LogManager(Level.INFO, "finished");
    }
	/***
	 * job执行异常时调用
	 * 
	 * @param e
	 */
	public void exceptionJob(DatahubException ex){
	    sendMsg2LogManager(Level.ERROR, "error:\n"+ex.getMessage());
	    LOGGER.error(job.getJobCode(), ex);
	}
	
    public JobDataMap getDataMap() {
        return dataMap;
    }

    public JobInfo getJob() {
        return job;
    }
    
    public JobLog getJobLog() {
        return log;
    }

    public Long getTimeOut() {
        return timeOut;
    }


}
