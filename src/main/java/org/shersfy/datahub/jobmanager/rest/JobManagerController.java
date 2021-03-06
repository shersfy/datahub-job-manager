package org.shersfy.datahub.jobmanager.rest;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.rest.form.JobInfoForm;
import org.shersfy.datahub.jobmanager.service.JobInfoService;
import org.shersfy.datahub.jobmanager.service.JobLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class JobManagerController extends BaseController{
    
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Value("${version}")
    private String version = "";
    
    @Resource
    private JobInfoService jobInfoService;
    
    @Resource
    private JobLogService jobLogService;
    
    @GetMapping("/")
    public Object index() {
        return "Welcom Datahub Job Manager Application "+ version;
    }
    
    @GetMapping("/api/v1/job/submit")
    public Object submitJob(@Valid JobInfoForm form, BindingResult bundle) {
        // 本地check
        Result res = check(form, bundle);
        if(res.getCode() != SUCESS){
            return res;
        }
        // 提交作业
        JobInfo info = initJobInfo(form);
        res = jobInfoService.saveJob(info);
        
        return formatMsg(res);
    }
    
    /**任务--查看任务详情*/
    @GetMapping("/api/v1/job/list")
    public Object listJobs(JobInfoForm form){
        
        JobInfo where = new JobInfo();
        where.setJobName(form.getJobName());
        where.setConfig(form.getConfig());
        
        int pageNum  = form.getPageNo();
        int pageSize = form.getPageSize();
        
        List<Integer> types = null;
        List<Long> pids     = null;
        List<Long> uids     = null;
        
        List<?> data = jobInfoService.findPageVo(where, pageNum, pageSize, types, pids, uids).getData();
        
        return data;
    }
    
    @GetMapping("/api/v1/job/enable")
    public Object enbaleJob(Long id) {
        Result res = jobInfoService.enableJob(id);
        return formatMsg(res);
    }
    
    @GetMapping("/api/v1/job/retry")
    public Object retryOnce(Long logId) {
        Result res = jobInfoService.retryOnce(logId);
        return formatMsg(res);
    }
    
    @GetMapping("/api/v1/job/disable")
    public Object disableJob(Long id) {
        Result res = jobInfoService.disableJob(id);
        return formatMsg(res);
    }
    
    @GetMapping("/api/v1/job/delete")
    public Object deleteJob(Long id) {
        Result res = jobInfoService.deleteJob(id);
        return formatMsg(res);
    }
    
    @GetMapping("/api/v1/log/update")
    public Object updateLog(Long logId, int status) {
        Result res = jobLogService.updateLog(logId, status);
        return formatMsg(res);
    }
    
}
