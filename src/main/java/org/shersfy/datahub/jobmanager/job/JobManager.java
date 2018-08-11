package org.shersfy.datahub.jobmanager.job;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.jobmanager.constant.Const.JobType;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 作业任务管理器
 */
@Order(100)
@Component("jobManager")
public class JobManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);
    public static ExecutorService SUB_THREAD_POOL_ONCE = null;

    @Value("${jobSubThreadSize}")
    private int jobSubThreadSize;
    
    @Resource(name="schedulerFactory")
    private Scheduler scheduler;

    /**
     * 启动任务调度
     * 
     * @throws SchedulerException
     */
    @PostConstruct
    public void start() throws SchedulerException{
        LOGGER.info("Quartz Scheduler starting ...");
        SUB_THREAD_POOL_ONCE = Executors.newFixedThreadPool(jobSubThreadSize);
        scheduler.start();
        LOGGER.info("Quartz Scheduler started");
    }

    /**
     * 关闭任务调度
     * 
     * @throws SchedulerException
     */
    public void stop() throws SchedulerException{
        if(scheduler.isShutdown()){
            return;
        }
        scheduler.shutdown();
    }

    /**
     * 添加一个任务到调度
     * 
     * @param model
     * @param dataMap
     * @return Date
     * @throws DatahubException
     */
    @SuppressWarnings("unchecked")
    public Date addJob(JobInfo model, JobDataMap dataMap) 
        throws DatahubException{

        String group = JobType.valueOf(model.getJobType()).name();
        try {
            if(!scheduler.isStarted()){
                this.start();
            }
            // 加载job任务执行类
            Class<? extends Job> jobClzz = (Class<? extends Job>) Class.forName(model.getJobClass());
            // 创建触发器
            Trigger trigger = TriggerFactory.build(model);
            // 创建Job详情对象
            JobDetail jobDetail = JobBuilder.newJob(jobClzz)
                .withIdentity(model.getJobCode(), group)
                .setJobData(dataMap)
                .build();
            return scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception ex) {
            String err = "add job error: job key(%s, %s)";
            throw DatahubException.throwDatahubException(String.format(err, model.getJobCode(), group), ex);
        }

    }

    /**
     * 计算任务首次执行时间
     * 
     * @param model
     * @return Date
     * @throws DatahubException
     */
    public Date computeFirstFireTime(JobInfo model) 
        throws DatahubException{

        try {
            if(!scheduler.isStarted()){
                this.start();
            }
            // 创建触发器
            Trigger trigger = TriggerFactory.build(model);
            OperableTrigger trig = (OperableTrigger)trigger;
            if(trig == null){
                throw new DatahubException("calculate the first execution time error, the trigger is null");
            }
            Date first = trig.computeFirstFireTime(null);
            return first;
        } catch (Exception ex) {
            String err = "calculate the first execution time error: cron=%s";
            throw DatahubException.throwDatahubException(String.format(err, model.getCronExpression()), ex);
        }

    }
    /**
     * 计算任务下一次执行时间
     * 
     * @param model
     * @return Date
     * @throws DatahubException
     */
    public Date getNextFireTime(JobInfo model) 
        throws DatahubException{

        try {
            if(!scheduler.isStarted()){
                this.start();
            }
            // 创建触发器
            Trigger trigger = TriggerFactory.build(model);
            OperableTrigger trig = (OperableTrigger)trigger;
            if(trig == null){
                throw new DatahubException("calculate the first execution time error, the trigger is null");
            }
            Date next = trig.getNextFireTime();
            return next;
        } catch (Exception ex) {
            String err = "calculate the first execution time error: cron=%s";
            throw DatahubException.throwDatahubException(String.format(err, model.getCronExpression()), ex);
        }

    }
    /**
     * 从调度移除一个任务
     * 
     * @return boolean
     * @throws DatahubException
     */
    public boolean removeJob(String jobCode, JobType type) throws DatahubException{
        JobKey key = new JobKey(jobCode, type.name());
        try {
            return scheduler.deleteJob(key);

        } catch (Exception ex) {
            String err = "the scheduler removes the job error: job key(%s, %s)";
            throw DatahubException.throwDatahubException(String.format(err, key.getName(), key.getGroup()), ex);
        }
    }

    /**
     * 
     * 修改job
     * @param info
     * @param dataMap
     * @return true:修改成功，false:修改job不存在
     * @throws DatahubException
     */
    public boolean modifyJob(JobInfo info, JobDataMap dataMap) 
        throws DatahubException{
        
        String jobCode = info.getJobCode();
        JobType type  = JobType.valueOf(info.getJobType());

        if(checkExists(info.getJobCode(), type)){
            this.removeJob(jobCode, type);
            this.addJob(info, dataMap);
        }
        else{
            this.addJob(info, dataMap);
        }
        return false;
    }

    /**
     * 检查定时任务是否处于调度中
     * 
     * @param key 任务key
     * @return boolean
     * @throws SchedulerException
     */
    public boolean checkExists(String jobCode, JobType type) 
        throws DatahubException{
        JobKey key = new JobKey(jobCode, type.name());
        try {
            return scheduler.checkExists(key);

        } catch (Exception ex) {
            String err = "the scheduler check exists job error: job key(%s, %s)";
            throw DatahubException.throwDatahubException(String.format(err, key.getName(), key.getGroup()), ex);
        }
    }

    /**
     * 获取调度
     * 
     * @return Scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * 恢复调度</br>
     * If any of the Job'sTrigger s missed one or more fire-times, then the Trigger's misfire instruction will be applied.
     * 
     * @param jobCode
     * @param type
     * @throws DatahubException
     */
    public void resumeJob(String jobCode, JobType type) throws DatahubException{
        try {
            scheduler.resumeJob(new JobKey(jobCode, type.name()));

        } catch (Exception e) {
            throw new DatahubException(e);
        }
    }

    public void pauseJob(String jobCode, JobType type) throws DatahubException{
        try {
            scheduler.pauseJob(new JobKey(jobCode, type.name()));

        } catch (Exception e) {
            throw new DatahubException(e);
        }
    }

    /**
     * 立刻执行一次
     * 
     * @param info
     * @param map
     * @throws DatahubException
     */
    public void onceImmediate(JobInfo info, JobDataMap map) 
        throws DatahubException{

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends Job> jobClzz = (Class<? extends Job>) Class.forName(info.getJobClass());
                    Date date = new Date();

                    JobDetail jobDetail = JobBuilder.newJob(jobClzz)
                        .withIdentity(info.getJobCode(), JobType.valueOf(info.getJobType()).name())
                        .setJobData(map)
                        .build();

                    TriggerFiredBundle firedBundle = new TriggerFiredBundle(jobDetail , 
                        (OperableTrigger) TriggerFactory.build(info), null , false,  date, date, date, date);
                    JobExecutionContext context = new JobExecutionContextImpl(null, firedBundle , null);
                    Object job = jobClzz.newInstance();
                    Method method = jobClzz.getMethod("execute", JobExecutionContext.class);
                    method.invoke(job, context);
                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }
        });
        th.setName("OnceImmediate");
        SUB_THREAD_POOL_ONCE.execute(th);
    }
}
