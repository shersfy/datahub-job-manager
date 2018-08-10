package org.shersfy.datahub.jobmanager.model;

import java.util.Date;

public class JobInfo extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 主键 **/
    private Long id;

    /** 项目ID **/
    private Long pid;

    /** 用户ID **/
    private Long userId;

    /** 任务类型 **/
    private Integer jobType;

    /** 任务名称 **/
    private String jobName;

    /** 任务唯一编码 **/
    private String jobCode;

    /** 处理类名 **/
    private String jobClass;

    /** 0:立即执行1次，1:周期性(cron表达式) **/
    private Integer periodType;

    /** 生效时间 **/
    private Date effectiveTime;

    /** 失效时间 **/
    private Date ineffectiveTime;

    /** 启动延迟 **/
    private Long startDelay;

    /** cron表达式 **/
    private String cronExpression;

    /** 调度状态(0：等待，1：调度中(默认)，2：调度完成) **/
    private Integer status;

    /** 启用禁用(0:启用(默认)，1：禁用) **/
    private Boolean disable;

    /** 备注 **/
    private String note;

    /** 配置参数 **/
    private String config;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Integer getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getIneffectiveTime() {
        return ineffectiveTime;
    }

    public void setIneffectiveTime(Date ineffectiveTime) {
        this.ineffectiveTime = ineffectiveTime;
    }

    public Long getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(Long startDelay) {
        this.startDelay = startDelay;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}