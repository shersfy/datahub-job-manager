package org.shersfy.datahub.jobmanager.rest.form;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.shersfy.datahub.commons.params.template.HdfsParams;
import org.shersfy.datahub.jobmanager.constant.Const;
import org.shersfy.datahub.jobmanager.constant.Const.JobPeriod;
import org.shersfy.datahub.jobmanager.constant.Const.JobTypes;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.alibaba.fastjson.JSON;

public class JobInfoForm extends BaseForm{
    /** 任务名称 **/
	@NotBlank(message=MSGT0001E000001)
	@Length(min=1, max=255, message=MSGT0001E000006)
    private String jobName;
	
	/** 源类型 **/
	@Min(value=0, message=MSGT0001E000008)
	private String fromType;
	
	 /** 数据源参数 **/
	private String fromJson;
	
    /** 来源ID **/
	@Min(value=1, message=MSGT0001E000008)
    private String fromId;
	
	/** 目标类型 **/
	@Min(value=0, message=MSGT0001E000008)
	private String toType;
    
    /** 目标ID **/
    @NotBlank(message=MSGT0001E000001)
    @Min(value=1, message=MSGT0001E000008)
    private String toId;
    
    /** 0:立即执行1次，1:定时执行1次(cron表达式)，2:周期性(cron表达式) **/
    @Min(value=0, message=MSGT0001E000008)
    @Max(value=2, message=MSGT0001E000009)
    private Integer periodType;
    /** cron表达式 **/
    private String cronExpression;
    /** 目标hdfs参数 **/
    private String toHdfsJson;
    /** 目标hive参数 **/
    private String toHiveJson;
    /** 备注 **/
    private String note;
    
	@Override
	public BindingResult check(BindingResult check) {
		if(check.hasErrors()){
			return check;
		}

		if(StringUtils.isBlank(cronExpression)){
			cronExpression = Const.CRON_DEFAULT;
		}
		
		if(Const.CRON_DEFAULT.equals(cronExpression.trim())){
			// 立即执行任务
			periodType = JobPeriod.PeriodOnceImmed.index();
		} else {
			// 周期任务
			periodType = JobPeriod.PeriodCircle.index();
		}
		
		if(StringUtils.isBlank(toType)){
			toType = String.valueOf(JobTypes.Dummy.index());
		}
		
		
		ObjectError error = null;
		JobTypes toType = JobTypes.valueOf(Integer.parseInt(this.getToType()));
		if(JobTypes.HDFS == toType){

			if(StringUtils.isBlank(toHdfsJson)){
				error = new ObjectError("toHdfsJson", new String[]{MSGT0001E000001}, null, "");
				check.addError(error);
				return check;
			}
			
			List<HdfsParams> toHdfsList = null;
			try {
				toHdfsList = JSON.parseArray(getToHdfsJson(), HdfsParams.class);
			} catch (Exception e) {
				error = new ObjectError("toHdfsJson", new String[]{MSGT0001E000003}, null, "");
				check.addError(error);
				return check;
			}

			if(toHdfsList == null){
				error = new ObjectError("toHdfsJson", new String[]{MSGT0001E000003}, null, "");
				check.addError(error);
				return check;
			}

			for(HdfsParams p :toHdfsList){
				if(StringUtils.isBlank(p.getFileName())){
					error = new ObjectError("fileName", new String[]{MSGT0001E000001}, null, "");
					check.addError(error);
					return check;
				}
				if(p.getFileName().length()>255){
					error = new ObjectError("fileName", new String[]{MSGT0001E000006}, new Integer[]{1, 255}, "");
					check.addError(error);
					return check;
				}
			}
			
			toHiveJson = "[]";

		} else if(JobTypes.Hive == toType || JobTypes.HiveSpark == toType){

			if(StringUtils.isBlank(getToHiveJson())){
				error = new ObjectError("toHiveJson", new String[]{MSGT0001E000001}, null, "");
				check.addError(error);
				return check;
			}
			toHdfsJson = "[]";
		}
		
		return check;
	}

	public String getJobName() {
		return jobName;
	}

	public String getFromId() {
		return fromId;
	}

	public String getToId() {
		return toId;
	}

	public String getToType() {
		return toType;
	}

	public Integer getPeriodType() {
		return periodType;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public void setPeriodType(Integer periodType) {
		this.periodType = periodType;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getToHdfsJson() {
		return toHdfsJson;
	}

	public String getToHiveJson() {
		return toHiveJson;
	}

	public void setToHdfsJson(String toHdfsJson) {
		this.toHdfsJson = toHdfsJson;
	}

	public void setToHiveJson(String toHiveJson) {
		this.toHiveJson = toHiveJson;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getFromJson() {
		return fromJson;
	}

	public void setFromJson(String fromJson) {
		this.fromJson = fromJson;
	}

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
