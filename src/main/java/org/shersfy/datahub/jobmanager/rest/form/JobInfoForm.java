package org.shersfy.datahub.jobmanager.rest.form;


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
	
	/** 数据源任务类型 **/
	@NotBlank(message=MSGT0001E000001)
	private String jobType;
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
		if(JobType.valueOfAlias(jobType) == JobType.Dummy) {
		    error = new ObjectError(jobType, new String[]{MSGT0027E000005}, null, "");
		    check.addError(error);
		    return check;
		}
		
		if(getPid() == null) {
		    error = new ObjectError("pid", new String[]{MSGT0001E000001}, null, "");
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


    public String getJobType() {
        return jobType;
    }


    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

}
