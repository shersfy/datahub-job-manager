package org.shersfy.datahub.jobmanager.rest;


import javax.validation.constraints.NotBlank;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.shersfy.datahub.jobmanager.constant.Const;
import org.shersfy.datahub.jobmanager.constant.Const.JobType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class JobInfoForm extends BaseForm{
    
    /** 任务名称 **/
	@NotBlank(message=MSGT0001E000001)
	@Length(min=1, max=255, message=MSGT0001E000006)
    private String jobName;
	
	/** 源类型 **/
	@NotBlank(message=MSGT0001E000001)
	private String fromType;
	
	/** 目标类型 **/
	@NotBlank(message=MSGT0001E000001)
	private String toType;
    
    /** false:立即执行1次，true:周期性(cron表达式) **/
    private boolean cyclicity;
    
    /** cron表达式 **/
    private String cronExpression;
    /** 参数配置 **/
    private String config;
    /** 备注 **/
    private String note;
    
	@Override
	public BindingResult check(BindingResult check) {
		if(check.hasErrors()){
			return check;
		}

		if(StringUtils.isBlank(cronExpression) || !cyclicity){
			cronExpression = Const.CRON_DEFAULT;
		}
		
		ObjectError error = null;
		if(JobType.valueOfAlias(fromType) == JobType.Dummy) {
		    error = new ObjectError("fromType", new String[]{MSGT0027E000005}, new String[]{fromType}, "");
		    check.addError(error);
		    return check;
		}
		
		if(JobType.valueOfAlias(toType) == JobType.Dummy) {
		    error = new ObjectError("toType", new String[]{MSGT0027E000005}, new String[]{toType}, "");
		    check.addError(error);
		    return check;
		}
		
		return check;
	}


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public boolean isCyclicity() {
        return cyclicity;
    }

    public void setCyclicity(boolean cyclicity) {
        this.cyclicity = cyclicity;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
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
