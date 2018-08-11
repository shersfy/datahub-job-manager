package org.shersfy.datahub.jobmanager.model;

import java.util.Date;

public class JobLog extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /** 任务ID **/
    private Long jobId;

    /** 结果状态(1:成功，2：失败) **/
    private Integer status;

    /** 开始时间 **/
    private Date startTime;

    /** 结束时间 **/
    private Date endTime;

    /** 详细信息 **/
    private String detail;
    
    /** 文件大小 **/
    private Long fileSize;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public Long getFileSize() {
		return fileSize;
	}
    
    public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}