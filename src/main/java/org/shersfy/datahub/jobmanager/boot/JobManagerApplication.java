package org.shersfy.datahub.jobmanager.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@ComponentScan("org.shersfy.datahub.jobmanager")
@MapperScan("org.shersfy.datahub.jobmanager.mapper")
@SpringBootApplication
public class JobManagerApplication {

	public static void main(String[] args) {
	    
		SpringApplication.run(JobManagerApplication.class, args);
	}

}
