package org.shersfy.datahub.service.jobmanager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.shersfy.datahub.commons.beans.Page;
import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.jobmanager.constant.Const.CronType;
import org.shersfy.datahub.jobmanager.constant.Const.JobPeriod;
import org.shersfy.datahub.jobmanager.constant.Const.JobTypes;
import org.shersfy.datahub.jobmanager.mapper.BaseMapper;
import org.shersfy.datahub.jobmanager.mapper.JobInfoMapper;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.vo.BaseVo;
import org.shersfy.datahub.jobmanager.model.vo.JobInfoVo;
import org.shersfy.datahub.jobmanager.service.JobInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("jobInfoService")
public class JobInfoServiceImpl extends BaseServiceImpl<JobInfo, Long> 
	implements JobInfoService {
    
    @Resource
    private JobInfoMapper mapper;
    
    @Override
    public BaseMapper<JobInfo, Long> getMapper() {
        return mapper;
    }

    @Override
    public Page<JobInfoVo> findPageVo(JobInfo where, int pageNum, int pageSize, 
                                      List<Integer> types, List<Long> pids, List<Long> uids) {
        
        Map<String, Object> map = parseMap(where);
        map.put("types", types);
        map.put("pids", pids);
        map.put("uids", uids);
        
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
        JobTypes srcType = JobTypes.valueOf(po.getJobType());
        vo.setJobTypeName("job.type."+srcType.alias());

        //周期
        JobPeriod type1 = JobPeriod.valueOf(po.getPeriodType());
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
                vo.setPeriodStr("job.period."+JobPeriod.PeriodOnceImmed.name());
                break;
            }
            break;
            //定时执行
        case PeriodOnceOntime:
            vo.setPeriodStr("job.period."+JobPeriod.PeriodOnceOntime.name());
            break;
            //立即执行
        case PeriodOnceImmed:
        default:
            vo.setPeriodStr("job.period."+JobPeriod.PeriodOnceImmed.name());
            break;
        }

        return vo;
    }

    @Override
    public Result saveJob(JobInfo info) {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    public boolean isAvailable(JobInfo job) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void initAll() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initJobLogs(Long jobId, String because) {
        // TODO Auto-generated method stub
        
    }

}
