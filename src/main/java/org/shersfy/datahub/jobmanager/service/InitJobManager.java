package org.shersfy.datahub.jobmanager.service;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component("initJobManager")
public class InitJobManager {
	
	private final Logger LOGGER = LoggerFactory.getLogger(InitJobManager.class);
	
	public static Map<String, Object> BEAN_CONTAINER 	= null;
	@Resource
	private JobInfoService jobInfoService;
//	@Resource
//	private TimerJobCleanTmpFiles timerJobCleanTmpFiles;
//	@Resource
//	private TimerJobDisableWaitimeoutJobs timerJobDisableWaitimeoutJobs;
	
	
	/**注册service bean到全局容器**/
	private void registerBeans() {
		LOGGER.info("datahub runtime register beans");
		BEAN_CONTAINER = new HashMap<>();
		BEAN_CONTAINER.put(InitJobManager.class.getName(), this);
		BEAN_CONTAINER.put(JobInfoService.class.getName(), jobInfoService);
	}
	
	@PostConstruct
	private void initAll(){
		// datahu
		LOGGER.info("datahub runtime initializing ...");
		this.initGlobalVariables();
		this.registerBeans();
		this.initTimerJobs();
		LOGGER.info("datahub runtime initialized.");
	}
	
	/**初始化全局变量**/
	private void initGlobalVariables(){
		LOGGER.info("datahub runtime init global variables ...");
	}
	
	/**初始化定时任务**/
	private void initTimerJobs(){
	    LOGGER.info("datahub runtime init clean temporary files job ...");
	}

	public static <T> T getBean(Class<T> beanClass) {
		if(beanClass == null){
			return null;
		}
		@SuppressWarnings("unchecked")
		T bean = (T) BEAN_CONTAINER.get(beanClass.getName());
		return bean;
	}

}
