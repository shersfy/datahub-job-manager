package org.shersfy.datahub.jobmanager.rest;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.service.JobInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobManagerController extends BaseController{
    
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Value("${version}")
    private String version = "";
    
    @Resource
    private JobInfoService jobInfoService;
    
    @GetMapping("/")
    public Object index() {
        return "Welcom Datahub Job Manager Application "+ version;
    }
    
    @PostMapping("/api/v1/job/submit")
    public Object submitJob(@Valid JobInfoForm form, BindingResult bundle) {
        // 本地check
        Result res = check(form, bundle);
        if(res.getCode() != SUCESS){
            return res;
        }
        // 远程服务参数check
        res = jobInfoService.remoteCheck(form.getConfig());
        if(res.getCode() != SUCESS){
            return res;
        }
        
        // 提交作业
        JobInfo info = initJobInfo(form);
        res = jobInfoService.saveJob(info);
        
        return res;
    }
    
    /**任务--查看任务详情*/
    @GetMapping("/api/v1/job/list")
    public Object listJobs(@Valid JobInfoForm form, BindingResult bundle){
        
        Result res = check(form, bundle);
        if(res.getCode() != SUCESS){
            return res;
        }
        
        JobInfo where = new JobInfo();
        where.setJobName(form.getJobName());
        where.setConfig(form.getConfig());
        
        int pageNum  = form.getPageNo();
        int pageSize = form.getPageSize();
        
        List<Integer> types = null;
        List<Long> pids     = null;
        List<Long> uids     = null;
        
        List<?> data = jobInfoService.findPageVo(where, pageNum, pageSize, types, pids, uids).getData();
        res.setModel(data);
        
        return res;
    }
    
    
}
