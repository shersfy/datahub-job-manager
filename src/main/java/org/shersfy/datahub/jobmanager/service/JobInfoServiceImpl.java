package org.shersfy.datahub.jobmanager.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.JobDataMap;
import org.shersfy.datahub.commons.beans.Page;
import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.commons.beans.ResultMsg;
import org.shersfy.datahub.commons.constant.CommConst;
import org.shersfy.datahub.commons.constant.JobConst.CronType;
import org.shersfy.datahub.commons.constant.JobConst.JobLogStatus;
import org.shersfy.datahub.commons.constant.JobConst.JobPeriodType;
import org.shersfy.datahub.commons.constant.JobConst.JobStatus;
import org.shersfy.datahub.commons.constant.JobConst.JobType;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.commons.meta.LogMeta;
import org.shersfy.datahub.commons.meta.MessageData;
import org.shersfy.datahub.commons.utils.DateUtil;
import org.shersfy.datahub.jobmanager.feign.DhubDbExecutorClient;
import org.shersfy.datahub.jobmanager.feign.ServicesFeignClient;
import org.shersfy.datahub.jobmanager.i18n.I18nMessages;
import org.shersfy.datahub.jobmanager.mapper.BaseMapper;
import org.shersfy.datahub.jobmanager.mapper.JobInfoMapper;
import org.shersfy.datahub.jobmanager.model.BaseVo;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.JobInfoVo;
import org.shersfy.datahub.jobmanager.model.JobLog;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

@RefreshScope
@Transactional
@Service("jobInfoService")
public class JobInfoServiceImpl extends BaseServiceImpl<JobInfo, Long> 
    implements JobInfoService {

    @Value("${spring.quartz.jobCodePrefix}")
    private String jobCodePrefix;
    
    @Value("${spring.quartz.jobDispatchTimeoutSeconds}")
    private long jobDispatchTimeoutSeconds = 60 * 1;
   
    @Resource
    private JobInfoMapper mapper;
    
    @Resource
    private JobManager jobManager;
    @Resource
    private LogManager logManager;
    @Resource
    private JobLogService jobLogService;
    @Resource
    private DhubDbExecutorClient dhubDbExecutorClient;
    
    @Override
    public BaseMapper<JobInfo, Long> getMapper() {
        return mapper;
    }

    @Override
    public Page<JobInfoVo> findPageVo(JobInfo where, int pageNum, int pageSize, 
                                      List<Integer> types, List<Long> pids, List<Long> uids) {

        Map<String, Object> map = parseMap(where);
        map.put("types", types==null||types.isEmpty()?null:types);
        map.put("pids", pids==null||pids.isEmpty()?null:pids);
        map.put("uids", uids==null||uids.isEmpty()?null:uids);

        long count = mapper.findListCount(map);
        List<JobInfo> list = mapper.findList(map);

        Page<JobInfo> pagePo = new Page<>(pageNum, pageSize);
        pagePo.setTotalCount(count);
        pagePo.setData(list);

        Page<JobInfoVo> pageVo = Page.getPageInstance(JobInfoVo.class, pagePo);
        List<JobInfoVo> listVo = new ArrayList<JobInfoVo>();
        for(JobInfo po: pagePo.getData()){
            listVo.add(this.poToVo(po));
        }

        pageVo.setData(listVo);
        return pageVo;
    }


    @Override
    public JobInfoVo poToVo(JobInfo po) {
        if(po == null){
            return null;
        }

        JobInfoVo vo = BaseVo.newVoInstance(JobInfoVo.class, po);

        //任务类型
        JobType srcType = JobType.valueOf(po.getJobType());
        vo.setJobTypeName("job.type."+srcType.alias());

        //周期
        JobPeriodType type1 = JobPeriodType.valueOf(po.getPeriodType());
        switch (type1) {
            //周期类型:根据cron表达式解析周期
            case PeriodCircle:
                CronType type2 = CronType.valueOfByCron(po.getCronExpression());
                switch (type2) {
                    case PerHour:
                    case PerDay:
                    case PerMonth:
                    case PerWeek:
                        vo.setPeriodStr("job.period."+type2.name());
                        break;
                    default:
                        vo.setPeriodStr("job.period."+JobPeriodType.PeriodOnceImmed.name());
                        break;
                }
                break;
                //定时执行
            case PeriodOnceOntime:
                vo.setPeriodStr("job.period."+JobPeriodType.PeriodOnceOntime.name());
                break;
                //立即执行
            case PeriodOnceImmed:
            default:
                vo.setPeriodStr("job.period."+JobPeriodType.PeriodOnceImmed.name());
                break;
        }

        return vo;
    }

    @Override
    public Result saveJob(JobInfo info) {

        Result res  = new Result();
        JobInfo old = null;
        try {

            String jobCode = info.getJobCode();
            info.setJobCode(String.valueOf(System.nanoTime()));
            res = this.checkJobInfo(info);
            info.setJobCode(jobCode);
            
            if(res.getCode() != SUCESS){
                return res;
            }

            if(info.getId() != null){
                // 更新时check
                old = findById(info.getId());
                if(old==null){
                    res.setCode(FAIL);
                    res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobInfo", info.getId()));
                    return res;
                }

                // 调度中的任务先停止才能更新
                //1. 如果该任务在调度中，移除调度
                if(jobManager.checkExists(old.getJobCode(), JobType.valueOf(old.getJobType()))){
                     jobManager.removeJob(old.getJobCode(), JobType.valueOf(old.getJobType()));
                }
            }
            
            if(JobPeriodType.PeriodCircle.index() == info.getPeriodType()) {
                savePeriodJob(info);
            } else {
                saveOnceImmediateJob(info);
            }

            res.setModel(info);
        } catch (Throwable ex) {
            // 删除新建错误的job
            if(old==null && info.getId()!=null){
                deleteById(info.getId());
            }
            else if(old!=null){
                updateById(old);
            }
            res.setCode(FAIL);
            res.setMsg(I18nMessages.getCauseMsg(ex));
            if(res.getMsg().contains("will never fire")){
                String start    =  DateUtil.format(info.getStartTime(), CommConst.FORMAT_DATETIME);
                String end      =  DateUtil.format(info.getEndTime(), CommConst.FORMAT_DATETIME);
                Calendar cal = Calendar.getInstance();
                cal.setTime(info.getEndTime());
                if(cal.get(Calendar.YEAR)==9999){
                    end = "";
                }
                res.setI18nMsg(new ResultMsg(MSGT0027E000002, info.getCronExpression(), start, end));
            }
            else{
                res.setI18nMsg(I18nMessages.getI18nMsg(MSGT0027E000000, ex, info));
            }
            LOGGER.error(info.getJobCode(), ex);

        }
        return res;
    }

    /**
     * 插入或更新JobInfo记录
     * 
     * @param info
     * @return JobInfo
     * @throws DatahubException
     */
    private JobInfo insertOrUpdate(JobInfo info) throws DatahubException {
        //==> 生成jobCode jobtype.prefix+jobid
        StringBuffer jobCode = new StringBuffer(0);

        JobType type = JobType.valueOf(info.getJobType());
        // jobtype
        jobCode.append(type.alias()).append(".");
        // job id
        jobCode.append(jobCodePrefix);

        // JobKey
        info.setJobCode(jobCode.toString());

        //==> 1.1插入任务记录
        int cnt = 0;

        if(info.getId()==null){
            cnt = this.insert(info);
        }
        //更新时复用
        else{
            cnt = this.updateById(info);
        }
        if(cnt<1){
            throw new DatahubException("job record insert or update failed");
        }

        //==> 更新jobCode
        JobInfo udp = new JobInfo();
        udp.setId(info.getId());
        udp.setJobCode(jobCode.append(info.getId()).toString());
        this.updateById(udp);

        info.setJobCode(udp.getJobCode());
        info.setUpdateTime(udp.getUpdateTime());

        return info;
    }

    /**
     * 保存周期性任务
     * 
     * @param info 任务信息
     * @return
     * @throws DatahubException
     */
    private Result savePeriodJob(JobInfo info) throws DatahubException{

        Result res = new Result();
        // insert or update job
        info = this.insertOrUpdate(info);

        JobDataMap map = this.getMap(info);
        //添加到调度
        jobManager.addJob(info, map);

        // 校验是否添加成功
        if(!jobManager.checkExists(info.getJobCode(), 
            JobType.valueOf(info.getJobType()))) 
        {
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0027E000003, info.getJobCode()));
            return res;
        }
        res.setModel(info);

        return res;
    }

    /**
     * 保存一次性任务
     * 
     * @param jobClass
     * @param info
     * @return
     * @throws DatahubException
     */
    private Result saveOnceImmediateJob(JobInfo info) throws DatahubException{
        Result res = new Result();
        // insert or update job
        info = this.insertOrUpdate(info);
        JobDataMap dataMap = this.getMap(info);
        // 立刻执行1次
        jobManager.onceImmediate(info, dataMap);
        res.setModel(info);
        return res;
    }
    
    /**
     * put到map的值必须可以能序列化
     * @param info
     * @return
     */
    private JobDataMap getMap(JobInfo info){
        JobDataMap map = new JobDataMap();
        map.put("jobId", info.getId());
        map.put("job", info);
        map.put("jobDispatchTimeoutSeconds", jobDispatchTimeoutSeconds);
        return map;
    }


    @Override
    public Result checkExists(String jobCode, JobType type) {
        Result res = new Result();
        try {
            boolean flg = jobManager.checkExists(jobCode, type);
            res.setModel(flg);
        } catch (Throwable ex) {
            LOGGER.error("", ex);
            res.setCode(FAIL);
            res.setMsg(I18nMessages.getCauseMsg(ex));
            res.setI18nMsg(I18nMessages.getI18nMsg(MSGT0028E000000, ex, jobCode));
        }
        return res;
    }
    
    @Override
    public Result retryOnce(Long logId) {
        Result res = new Result();
        
        JobLog log = jobLogService.findById(logId);
        if(log == null){
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobLog", logId));
            return res;
        }
        
        JobInfo job = this.findById(log.getJobId());
        if(job == null){
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobInfo", log.getJobId()));
            return res;
        }
        
        try {
            JobDataMap map = this.getMap(job);
            jobManager.onceImmediate(job, map);
        } catch (Throwable ex) {
            LOGGER.error("", ex);
            res.setCode(FAIL);
            res.setMsg(I18nMessages.getCauseMsg(ex));
            res.setI18nMsg(I18nMessages.getI18nMsg(MSGT0030E000000, ex, job));
        }
        
        return res;
    }

    @Override
    public Result enableJob(Long id) {
        Result res = new Result();
        
        Date startTime = new Date();
        JobInfo info   = null;
        
        try {
            info = this.findById(id);
            if(info == null){
                res.setCode(FAIL);
                res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobInfo", id));
                return res;
            }
            
            // 已经开启
            if(jobManager.checkExists(info.getJobCode(), JobType.valueOf(info.getJobType()))){
                res.setI18nMsg(new ResultMsg(MSGT0030I000001, info.getJobCode()));
                return res;
            }

            JobDataMap map = this.getMap(info);
            JobPeriodType period = JobPeriodType.valueOf(info.getPeriodType());
            //立刻执行仅1此次
            if(period == JobPeriodType.PeriodOnceImmed){
                jobManager.onceImmediate(info, map);
            
            } else {
                // 更新状态
                JobInfo udp = new JobInfo();
                udp.setId(id);
                String msg = "";
                // 有效期判断
                if(this.isEffective(info)){
                    // 加入调度
                    jobManager.addJob(info, map);
                    
                    udp.setStatus(JobStatus.Normal.index());
                    udp.setDisable(false);
                    
                    msg = "The job %s has been enabled, and continues to be scheduled normally";
                    msg = String.format(msg, info.getJobCode());
               
                } else {
                    
                    udp.setStatus(JobStatus.Scheduled.index());
                    udp.setDisable(true);
                    
                    msg = "The job %s has been expired, [cron=%s, %s-%s]";
                    msg = String.format(msg, info.getJobCode(), info.getCronExpression(), 
                        DateUtil.format(info.getActiveTime(), CommConst.FORMAT_DATETIME), 
                        DateUtil.format(info.getExpireTime(), CommConst.FORMAT_DATETIME));
                }
                
                this.updateById(udp);
                
                // 写日志
                JobLog log = new JobLog();
                log.setJobId(info.getId());
                log.setStatus(JobLogStatus.Successful.index());
                log.setStartTime(startTime);
                log.setEndTime(new Date());
                jobLogService.insert(log);
                
                LOGGER.info(msg);
                logManager.sendMsg(new MessageData(new LogMeta(Level.INFO, msg)));
            }

            res.setModel(info);

        } catch (Throwable ex) {
            LOGGER.error("", ex);
            res.setCode(FAIL);
            res.setMsg(I18nMessages.getCauseMsg(ex));
            res.setI18nMsg(I18nMessages.getI18nMsg(MSGT0030E000000, ex, info));
        }

        return res;
    }

    @Override
    public Result disableJob(Long id) {
        
        Result res = new Result();
        
        Date startTime = new Date();
        JobInfo info   = null;
        
        try {
            info = this.findById(id);
            if(info == null){
                res.setCode(FAIL);
                res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobInfo", id));
                return res;
            }
            
            JobStatus status = JobStatus.valueOf(info.getStatus());
            JobType jobType  = JobType.valueOf(info.getJobType());
            // 正在调度
            if(status == JobStatus.Scheduling){
                res.setCode(FAIL);
                res.setI18nMsg(new ResultMsg(MSGT0029E000001, info.getJobCode()));
                return res;
            }
            
            // 在调度中，移除调度
            if(jobManager.checkExists(info.getJobCode(), jobType)){
                 jobManager.removeJob(info.getJobCode(), jobType);
            } else {
                res.setI18nMsg(new ResultMsg(MSGT0029I000001, info.getJobCode()));
            }
            
            JobInfo udp = new JobInfo();
            udp.setId(id);
            udp.setDisable(true);
            udp.setStatus(JobStatus.Scheduled.index());
            
            this.updateById(udp);
            
            // 写日志
            JobLog log = new JobLog();
            log.setJobId(info.getId());
            log.setStatus(JobLogStatus.Successful.index());
            log.setStartTime(startTime);
            log.setEndTime(new Date());
            jobLogService.insert(log);
            
            String msg = "The job %s has been disabled";
            msg = String.format(msg, info.getJobCode());
            LOGGER.info(msg);
            logManager.sendMsg(new MessageData(new LogMeta(Level.INFO, msg)));
            
            res.setModel(info);
            
        } catch (Throwable ex) {
            LOGGER.error("", ex);
            res.setCode(FAIL);
            res.setMsg(DatahubException.getCauseMsg(ex));
            res.setI18nMsg(I18nMessages.getI18nMsg(MSGT0029E000000, ex, info));
        }

        return res;
    }

    @Override
    public Result deleteJob(Long id) {
        // 移除调度
        Result res = disableJob(id);
        if(res.getCode() == FAIL){
            return res;
        }
        // 删除执行历史记录
        jobLogService.deleteByJobId(id);
        // 删除任务记录
        super.deleteById(id);

        LOGGER.info("deleted job and job logs, jobId={}", id);
        return res;
    }
    
    /**
     * 参数合法性check
     * 
     * @param info
     * @throws DatahubException 
     */
    public Result checkJobInfo(JobInfo info) throws DatahubException{
        Result res = null;
        // 有效期check
        if(info.getActiveTime()==null) {
            info.setActiveTime(new Date());
        }
        if(info.getExpireTime()==null) {
            info.setExpireTime(new Date(CommConst.MAX_DATE));
        }
        if(!isEffective(info)){
            res = new Result();
            String active = DateUtil.format(info.getActiveTime(), CommConst.FORMAT_DATETIME);
            String expire = DateUtil.format(info.getExpireTime(), CommConst.FORMAT_DATETIME);
            Calendar cal = Calendar.getInstance();
            cal.setTime(info.getActiveTime());
            if(cal.get(Calendar.YEAR)==9999){
                active = "";
            }
            
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0027E000002, info.getCronExpression(), active, expire));
            return res;
        }
        
        // 调用远程服务对配置参数check
        return this.remoteCheck(info);
    }

    @Override
    public boolean isEffective(JobInfo job) {

        if(job==null){
            return false;
        }
        
        if(JobPeriodType.PeriodOnceImmed.index() == job.getPeriodType()) {
            return true;
        }

        Date systime = new Date();
        Date endtime = job.getExpireTime();
        if(DateUtil.compareDate(systime, endtime)>0){
            return false;
        }
        // cron表达式验证
        try {
            if(jobManager.computeFirstFireTime(job) == null){
                return false;
            }

        } catch (DatahubException de) {
            LOGGER.error("", de);
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public void initAll() {

    }

    @Override
    public Result remoteCheck(JobInfo info) {

        ServicesFeignClient client = getServicesFeignClient(JobType.valueOf(info.getJobType()));
        if(client==null) {
            Result res = new Result(FAIL, "not support job type: "+info.getJobType());
            return res;
        }
        String text = client.callCheckJobConfig(info.getConfig());
        return JSON.parseObject(text, Result.class);
    }

    @Override
    public ServicesFeignClient getServicesFeignClient(JobType type) {
        ServicesFeignClient client = null;
        switch (type) {
            case LocalUpload:
                break;
            case DatabaseMove:
            case AliyunRDS:
            case AmazonRDS:
                client = dhubDbExecutorClient;
                break;
            case ClientUpload:
                break;
            case DumpUpload:
                break;
            case S3Upload:
                break;
            case FtpUpload:
                break;
            case CsvUpload:
                break;
            case ExcelUpload:
                break;
            case HDFS:
            case Hive:
            case HiveSpark:
                break;
            default:
                break;
        }
        return client;
    }

    @Override
    public JobLogService getJobLogService() {
        return jobLogService;
    }

    @Override
    public LogManager getLogManager() {
        return logManager;
    }

}
