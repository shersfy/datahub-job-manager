package org.shersfy.datahub.jobmanager.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.shersfy.datahub.commons.constant.CommConst;
import org.shersfy.datahub.commons.constant.JobConst.JobLogStatus;
import org.shersfy.datahub.commons.constant.JobConst.JobPeriodType;
import org.shersfy.datahub.commons.constant.JobConst.JobStatus;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.commons.exception.ExpiredException;
import org.shersfy.datahub.commons.meta.MessageData;
import org.shersfy.datahub.commons.utils.DateUtil;
import org.shersfy.datahub.commons.utils.JobLogUtil;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.JobLog;
import org.shersfy.datahub.jobmanager.service.InitJobManager;
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

    /**
     * job执行前调用
     * @param context
     * @throws DatahubException 
     * @throws ExpiredException 
     */
    public void beforeJob(JobExecutionContext context) throws DatahubException, ExpiredException{
        jobInfoService = InitJobManager.getBean(JobInfoService.class);
        jobLogService  = jobInfoService.getJobLogService();
        logManager     = jobInfoService.getLogManager();

        dataMap = context.getJobDetail().getJobDataMap();
        
        Long jobId = dataMap.getLong("jobId");
        timeOut    = dataMap.getLong("jobDispatchTimeoutSeconds");
        Long logId = dataMap.get("logId")==null?null:dataMap.getLong("logId");

        job = jobInfoService.findById(jobId);
        job = job==null?(JobInfo) dataMap.get("job"):job;

        LOGGER.info("jobId={}, logId={}, begining ...", job==null?"":job.getId(), log==null?"":log.getId());


        // 插入或更新执行记录
        if(logId == null) {
            log = new JobLog();
        } else {
            log = jobLogService.findById(logId);
        }
        if(expire() || log==null) {
            throw new ExpiredException("the job expired");
        }
        log.setJobId(job.getId());
        log.setStatus(JobLogStatus.Executing.index());
        log.setConfig(job.getConfig());
        log.setStartTime(new Date());
        log.setEndTime(log.getStartTime());
        
        if(logId == null) {
            jobLogService.insert(log);
            LOGGER.info("jobId={}, logId={}, insert job log record", job.getId(), log.getId());
        } else {
            jobLogService.updateById(log);
            LOGGER.info("jobId={}, logId={}, update job log record", job.getId(), log.getId());
        }

        JobInfo udp = new JobInfo();
        udp.setId(job.getId());
        udp.setStatus(JobStatus.Scheduling.index());
        jobInfoService.updateById(udp);
        LOGGER.info("jobId={}, logId={}, update job status scheduling", job.getId(), log.getId());
    }

    /**
     * 任务参数配置分发
     * @param context
     * @throws DatahubException
     */
    public abstract void dispatch(JobExecutionContext context) throws DatahubException;

    /**
     * job正常执行后调用
     *
     */
    public void afterJob() throws DatahubException{
        LOGGER.info("jobId={}, logId={}, execute successful", job.getId(), log.getId());
    }

    /***
     * job执行异常时调用
     * 
     * @param e
     */
    public void exceptionJob(DatahubException ex){
        if(ex instanceof ExpiredException) {
            String expire = DateUtil.format(job.getExpireTime(), CommConst.FORMAT_DATETIME);
            LOGGER.info("jobId={}, logId={}, expired {}, {}", job==null?"":job.getId(), 
                log==null?"":log.getId(), expire, job.getCronExpression());
            return;
        }
        LOGGER.error("jobId={}, logId={}, exception", job==null?"":job.getId(), log==null?"":log.getId());
        LOGGER.error("", ex);
    }

    private void finallyDo() {

        if(jobInfoService!=null&&job!=null&&job.getId()!=null) {
            
            boolean disable  = job.getDisable();
            JobStatus status = JobStatus.Normal;
            JobPeriodType period = JobPeriodType.valueOf(job.getPeriodType());

            // 一次性任务或过期
            if(period == JobPeriodType.PeriodOnceImmed || expire()) {
                status = JobStatus.Scheduled;
                disable = true;
            }
            JobInfo udp = new JobInfo();
            udp.setId(job.getId());
            udp.setStatus(status.index());
            udp.setDisable(disable);
            
            jobInfoService.updateById(udp);
            LOGGER.info("jobId={}, logId={}, update job status {}", job.getId(), 
                log==null?"":log.getId(), status.name().toLowerCase());
        }

        LOGGER.info("jobId={}, logId={}, finished", job==null?"":job.getId(), 
            log==null?"":log.getId());
    }

    /**
     * 任务有效期判定
     * @return
     */
    public boolean expire() {
        // 周期性任务有效期check 开始时间
        JobPeriodType period = JobPeriodType.valueOf(job.getPeriodType());
        if(JobPeriodType.PeriodCircle == period
            && DateUtil.compareDate(new Date(), job.getExpireTime())>0){
            return true;
        }
        return false;
    }

    /**发送日志到日志管理器**/
    public void sendMsg(Level level, String msg) {
        if(log==null || log.getId()==null) {
            LOGGER.info("job log is null, {}", msg);
            return;
        }
        
        String data = JobLogUtil.getMsgData(level, job.getId(), log.getId(), msg).toString();
        logManager.sendMsg(new MessageData(data));
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
