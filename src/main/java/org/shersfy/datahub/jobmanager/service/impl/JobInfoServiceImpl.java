package org.shersfy.datahub.jobmanager.service.impl;

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
import org.shersfy.datahub.commons.constant.ConstCommons;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.commons.utils.DateUtil;
import org.shersfy.datahub.jobmanager.constant.Const.CronType;
import org.shersfy.datahub.jobmanager.constant.Const.JobPeriodType;
import org.shersfy.datahub.jobmanager.constant.Const.JobType;
import org.shersfy.datahub.jobmanager.i18n.I18nMessages;
import org.shersfy.datahub.jobmanager.jobs.DispatchJob;
import org.shersfy.datahub.jobmanager.mapper.BaseMapper;
import org.shersfy.datahub.jobmanager.mapper.JobInfoMapper;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.vo.BaseVo;
import org.shersfy.datahub.jobmanager.model.vo.JobInfoVo;
import org.shersfy.datahub.jobmanager.service.JobInfoService;
import org.shersfy.datahub.jobmanager.service.component.JobManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("jobInfoService")
public class JobInfoServiceImpl extends BaseServiceImpl<JobInfo, Long> 
    implements JobInfoService {

    @Value("${jobCodePrefix}")
    private String jobCodePrefix;
    @Value("${dispatchJobTimeoutSeconds}")
    private int dispatchJobTimeoutSeconds = 60 * 1;
    @Resource
    private JobInfoMapper mapper;
    @Resource
    private JobManager jobManager;
    
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
            list.add(this.poToVo(po));
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

            res = this.checkJobInfo(info);
            if(res.getCode() != SUCESS){
                return res;
            }

            JobType type = JobType.valueOf(info.getJobType());
            if(type == null || type == JobType.Dummy){
                res.setCode(FAIL);
                res.setI18nMsg(new ResultMsg(MSGT0018I000001, "JobTypes", info.getJobType()));
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
            
            info.setJobClass(DispatchJob.class.getName());
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
                String start    =  DateUtil.format(info.getStartTime(), ConstCommons.FORMAT_DATETIME);
                String end      =  DateUtil.format(info.getEndTime(), ConstCommons.FORMAT_DATETIME);
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
    
    private JobDataMap getMap(JobInfo info){
        JobDataMap map = new JobDataMap();
        map.put("jobId", info.getId());
        map.put("job", info);
        map.put(JobInfoService.class.getName(), this);
        return map;
    }


    @Override
    public void startAllJobs() {
        // TODO Auto-generated method stub

    }

    @Override
    public Result checkExists(String jobCode, String groupNo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result enableJob(Long id, boolean isRestart) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result disableJob(Long id, boolean once) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<JobInfo> findAvailableJobs(JobInfo where) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean existByJobName(String jobName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Result deleteJob(Long id) {
        // TODO Auto-generated method stub
        return null;
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
        if(!isEffective(info)){
            res = new Result();
            String effective   = DateUtil.format(info.getEffectiveTime(), ConstCommons.FORMAT_DATETIME);
            String ineffective = DateUtil.format(info.getIneffectiveTime(), ConstCommons.FORMAT_DATETIME);
            Calendar cal = Calendar.getInstance();
            cal.setTime(info.getEndTime());
            if(cal.get(Calendar.YEAR)==9999){
                ineffective = "";
            }
            
            res.setCode(FAIL);
            res.setI18nMsg(new ResultMsg(MSGT0027E000002, info.getCronExpression(), effective, ineffective));
            return res;
        }
        
        // 调用远程服务对配置参数check
        return this.remoteCheck(info.getConfig());
    }

    @Override
    public boolean isEffective(JobInfo job) {

        if(job==null){
            return false;
        }

        Date systime = new Date();
        Date endtime = job.getEndTime();
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
        // TODO Auto-generated method stub

    }

    @Override
    public void initJobLogs(Long jobId, String because) {
        // TODO Auto-generated method stub

    }

    @Override
    public Result remoteCheck(String config) {
        // TODO Auto-generated method stub
        return null;
    }

}
