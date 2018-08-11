package org.shersfy.datahub.jobmanager.model;

import java.util.Date;

public class JobLog extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /** 任务ID **/
    private Long jobId;

    /** 结果状态(0：执行中(默认)，1：执行成功，2：执行失败) **/
    private Integer status;

    /** 开始时间 **/
    private Date startTime;

    /** 结束时间 **/
    private Date endTime;

    /** 日志存放路径 **/
    private String path;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}