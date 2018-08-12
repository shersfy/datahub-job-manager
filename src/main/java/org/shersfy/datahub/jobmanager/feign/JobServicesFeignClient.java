package org.shersfy.datahub.jobmanager.feign;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public interface JobServicesFeignClient {
    
    @RequestMapping(method = RequestMethod.GET, value = "/job/config")
    @ResponseBody
    String callJobConfig(@RequestParam("jobId")Long jobId, 
                         @RequestParam("logId")Long logId, @RequestParam("config")String config);

    @RequestMapping(method = RequestMethod.GET, value = "/job/check")
    @ResponseBody
    String callCheckJobConfig(@RequestParam("config")String config);

}
