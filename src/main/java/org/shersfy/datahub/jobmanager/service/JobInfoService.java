package org.shersfy.datahub.jobmanager.service;


import java.util.List;

import org.shersfy.datahub.commons.beans.Page;
import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.commons.constant.JobConst.JobType;
import org.shersfy.datahub.jobmanager.feign.JobServicesFeignClient;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.JobInfoVo;

/**
 * 任务管理接口
 */
public interface JobInfoService extends BaseService<JobInfo, Long> {

	/** 立刻执行仅一次 **/
	String ONCE_IMMED_CRON = "* * * * * ?";
	
	/**
	 * 分页查询 返回VO page
	 * 
	 * @param where
	 * @param page
	 * @return Page<JobInfoVo>
	 */
	public Page<JobInfoVo> findPageVo(JobInfo where, int pageNum, int pageSize, 
	                                  List<Integer> types, List<Long> pids, List<Long> uids);

	/**
	 * PO转换为VO
	 * 
	 * @param po
	 * @return JobInfoVo
	 */
	public JobInfoVo  poToVo(JobInfo po);

	/**
	 * 实现数据上传功能<br/>
	 * jobCode= 分组名.数据源类型.目标类型.任务名前缀.jobId(>5位)<br/>
	 * groupStr={@link JobType}<br/>
	 * 
	 * @param
	 * @param info定时任务对象
	 * @return Result
	 */
	public Result saveJob(JobInfo info);


	/**
	 * check任务是否处于调度中, 返回
	 * code:200成功，0失败
	 * model:true存在，false不存在
	 * msg:执行信息
	 * 
	 * @param jobCode
	 * @param type
	 * @return Result
	 */
	public Result checkExists(String jobCode, JobType type);

	/**
	 * 启用调度
	 * 
	 * @param id
	 * @return Result
	 */
	public Result enableJob(Long id);

	/**
	 * 禁用调度
	 * 
	 * @param id 任务ID
	 * @param once 是否停止本次任务
	 * @return Result
	 */
	public Result disableJob(Long id);

	/**
	 * 删除任务
	 * 
	 * @param id
	 * @return
	 */
	public Result deleteJob(Long id);
	
	/**
	 * 有效期判断, 返回true有效,false失效
	 * 
	 * @param job 任务信息
	 * @return boolean
	 */
	public boolean isEffective(JobInfo job);
	/**
	 * 初始化执行
	 */
	public void initAll();
	
	/**
	 * 远程服务check配置参数
	 * @param config
	 * @return
	 */
    public Result remoteCheck(JobInfo info);
    
    public JobServicesFeignClient getServicesFeignClient(JobType type);
    
    public JobLogService getJobLogService();

    public LogManager getLogManager();

}
