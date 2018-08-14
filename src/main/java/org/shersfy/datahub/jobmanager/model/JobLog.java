package org.shersfy.datahub.jobmanager.model;


public class JobLog extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 任务ID **/
    private Long jobId;

    /** 结果状态(1：执行中(默认)，2：执行成功，3：执行失败) **/
    private Integer status;

    /** 日志存放路径 **/
    private String path;
    
    /** 历史配置参数 **/
    private String config;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

}