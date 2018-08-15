package org.shersfy.datahub.jobmanager.feign;

import org.shersfy.datahub.commons.beans.Result.ResultCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ServicesFeignClient {

    /**处理成功**/
    int SUCESS = ResultCode.SUCESS;
    /**处理失败**/
    int FAIL   = ResultCode.FAIL;

    @RequestMapping(method = RequestMethod.GET, value = "/job/check")
    @ResponseBody
    String callCheckJobConfig(@RequestParam("config")String config);


    @RequestMapping(method = RequestMethod.GET, value = "/job/execute")
    @ResponseBody
    String callExecuteJob(@RequestParam("jobId")Long jobId, 
                      @RequestParam("logId")Long logId, 
                      @RequestParam("config")String config);

}
