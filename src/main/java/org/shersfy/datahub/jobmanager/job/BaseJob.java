package org.shersfy.datahub.jobmanager.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.service.JobInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job基类
 */
public abstract class BaseJob implements Job{

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    private JobInfoService jobInfoService;
    
    private JobDataMap dataMap = null;
	private JobInfo job        = null;
	private Long timeOut       = null;

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
	}

	/**
	 * job正常执行后调用
	 *
	 */
	public void afterJob() throws DatahubException{
	    LOGGER.info("execute successful");
	}
	
	private void finallyDo() {
	    LOGGER.info("finished");
    }
	/***
	 * job执行异常时调用
	 * 
	 * @param e
	 */
	public void exceptionJob(DatahubException e){

	}

    public JobInfoService getJobInfoService() {
        return jobInfoService;
    }

    public JobDataMap getDataMap() {
        return dataMap;
    }

    public JobInfo getJob() {
        return job;
    }

    public Long getTimeOut() {
        return timeOut;
    }


}
