package org.shersfy.datahub.jobmanager.service;


import java.util.List;

import org.shersfy.datahub.commons.beans.Page;
import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.jobmanager.constant.Const.JobType;
import org.shersfy.datahub.jobmanager.feign.JobServicesFeignClient;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.JobInfoVo;

/**
 * 任务管理接口
 */
public interface JobInfoService extends BaseService<JobInfo, Long> {

	/** 立刻执行仅一次 **/
	public final String ONCE_IMMED_CRON = "* * * * * ?";
	
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
	 * 启动所有的Job
	 *
	 */
	public void startAllJobs();
	

	/**
	 * check任务是否处于调度中, 返回
	 * code:200成功，0失败
	 * model:true存在，false不存在
	 * msg:执行信息
	 * 
	 * @param jobCode
	 * @param groupNo
	 * @return Result
	 */
	public Result checkExists(String jobCode, String groupNo);

	/**
	 * 启用调度
	 * 
	 * @param id
	 * @param isRestart 是否服务重启
	 * @return Result
	 */
	public Result enableJob(Long id, boolean isRestart);

	/**
	 * 禁用调度
	 * 
	 * @param id 任务ID
	 * @param once 是否停止本次任务
	 * @return Result
	 */
	public Result disableJob(Long id, boolean once);

	/**
	 * 查询有效的任务<br/>
	 * 状态为调度中 且有效(有效结束时间>=当前时间)
	 * 
	 * @param where
	 * @return
	 */
	public List<JobInfo> findAvailableJobs(JobInfo where);
	
	/**
	 * 任务名是否存在, 返回true存在, false不存在
	 * @param jobName 任务名
	 * @return  boolean
	 */
	public boolean existByJobName(String jobName);
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
	 * 初始化结束执行中的日志状态
	 * 
	 * @param jobId 任务ID
	 * @param because 结束假执行中的原因
	 */
	public void initJobLogs(Long jobId, String because);

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
