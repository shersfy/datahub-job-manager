package org.shersfy.datahub.jobmanager.hystrix;

import org.shersfy.datahub.jobmanager.feign.DhubDbExecutorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 服务调用容错回调
 * @author py
 * @date 2018年8月11日
 */
@Component
public class DhubDbExecutorFallback implements DhubDbExecutorClient {
    
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public String callJobConfig(Long jobId, Long logId, String config) {
        LOGGER.error("jobId={}, logId={}, config={}", jobId, logId, config);
        return null;
    }

    @Override
    public String callCheckJobConfig(String config) {
        LOGGER.error("check params error, config={}", config);
        return null;
    }

}
