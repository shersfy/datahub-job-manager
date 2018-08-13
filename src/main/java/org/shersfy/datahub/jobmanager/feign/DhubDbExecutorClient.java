package org.shersfy.datahub.jobmanager.feign;

import org.shersfy.datahub.jobmanager.hystrix.FeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name=DhubDbExecutorClient.serviceId, 
fallbackFactory=FeignClientFallbackFactory.class)
public interface DhubDbExecutorClient extends ServicesFeignClient {
    
    String serviceId = "datahub-db-executor";
    
}
