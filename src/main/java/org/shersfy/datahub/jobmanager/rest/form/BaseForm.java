package org.shersfy.datahub.jobmanager.rest.form;

import java.text.ParseException;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.shersfy.datahub.commons.utils.DateUtil;
import org.shersfy.datahub.jobmanager.i18n.I18nCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.alibaba.fastjson.JSON;

/**
 * 表单参数公用
 */
public class BaseForm implements I18nCodes{

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /** 新建(为null)或更新ID **/
    @Min(value=1, message=MSGT0001E000008)
    private Long id;
    
    @Min(value=1, message=MSGT0001E000008)
    private Long pid;

    /** 访问token **/
    @NotBlank(message=MSGT0001E000001)
    @Length(min=1, max=255, message=MSGT0001E000006)
    private String token;

    /**开始时间**/
    private String startTimeStr;

    /**结束时间**/
    private String endTimeStr;
    /**当前页码**/
    private Integer pageNo = 1;
    /**分页大小**/
    private Integer pageSize = Integer.MAX_VALUE;


    public BindingResult check(BindingResult bundle){
        return bundle;
    }

    public Date getStartTime() {
        if(StringUtils.isEmpty(startTimeStr)){
            return null;
        }
        Date date = null;
        try {
            date = DateUtil.getDateByStr(startTimeStr+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {

        }
        return date;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public Date getEndTime() {
        if(StringUtils.isEmpty(endTimeStr)){
            return null;
        }
        Date date = null;
        try {
            date = DateUtil.getDateByStr(endTimeStr+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {

        }
        return date;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
