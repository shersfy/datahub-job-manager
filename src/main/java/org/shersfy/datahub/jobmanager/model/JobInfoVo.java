package org.shersfy.datahub.jobmanager.model;

/**
 * VO类
 */
@SuppressWarnings("serial")
public class JobInfoVo extends JobInfo {
	
	/**任务类型名称**/
	private String jobTypeName;
	
	/**调度情况 **/
	private String scheduleStatus;
	
	/**显示字符串 **/
	private String periodStr;
	
	/**任务所属项目名称 **/
	private String projName;

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getPeriodStr() {
        return periodStr;
    }

    public void setPeriodStr(String periodStr) {
        this.periodStr = periodStr;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

}
