package org.shersfy.datahub.jobmanager.job;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.shersfy.datahub.commons.constant.JobConst.JobType;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * 触发器工厂, 生产触发器
 */
public class TriggerFactory {
	

	private static CronTriggerFactoryBean ctf = new CronTriggerFactoryBean();
	
	/**
	 * 创建触发器对象
	 * 
	 * @param job
	 * @return Trigger
	 * @throws ParseException
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public static Trigger build(JobInfo job) 
			throws ParseException, ClassNotFoundException{
		
	    String group = JobType.valueOf(job.getJobType()).name();
		ctf.setName(job.getJobCode());
		ctf.setGroup(group);
		ctf.setCronExpression(job.getCronExpression());
		ctf.setStartTime(job.getStartTime());
		ctf.setStartDelay(job.getStartDelay());
		
		Class<? extends Job> jobClzz = (Class<? extends Job>) Class.forName(job.getJobClass());
		
		JobDetail jobDetail = JobBuilder.newJob(jobClzz)
                .withIdentity(job.getJobCode(), group)
                .build();
		ctf.setJobDetail(jobDetail);
		// 重新刷新赋值
		ctf.afterPropertiesSet();
		
		return ctf.getObject();
	}

}
