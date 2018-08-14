package org.shersfy.datahub.jobmanager.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients(basePackages="org.shersfy.datahub.jobmanager.feign")
@MapperScan("org.shersfy.datahub.jobmanager.mapper")
@ComponentScan(basePackages="org.shersfy.datahub.jobmanager", 
excludeFilters= @Filter(type=FilterType.ANNOTATION, value=FeignClient.class))
@SpringBootApplication
public class JobManagerApplication {

	public static void main(String[] args) {
	    
		SpringApplication.run(JobManagerApplication.class, args);
	}

}
