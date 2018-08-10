package org.shersfy.datahub.jobmanager.rest;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.shersfy.datahub.commons.beans.Result;
import org.shersfy.datahub.commons.beans.Result.ResultCode;
import org.shersfy.datahub.commons.beans.ResultMsg;
import org.shersfy.datahub.jobmanager.constant.Const;
import org.shersfy.datahub.jobmanager.i18n.I18nCodes;
import org.shersfy.datahub.jobmanager.i18n.I18nMessages;
import org.shersfy.datahub.jobmanager.i18n.PropertiesExt;
import org.shersfy.datahub.jobmanager.model.JobInfo;
import org.shersfy.datahub.jobmanager.model.vo.LoginUser;
import org.shersfy.datahub.jobmanager.rest.form.JobInfoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseController implements I18nCodes{

	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	private static ThreadLocal<HttpServletRequest> THREAD_LOCAL_REQUEST = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> THREAD_LOCAL_RESPONSE = new ThreadLocal<>();
	
	/**处理成功**/
	protected static final int SUCESS 	= ResultCode.SUCESS;
	/**处理失败**/
	protected static final int FAIL 	= ResultCode.FAIL;
	
	@ModelAttribute
	public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
		THREAD_LOCAL_REQUEST.set(request);
		THREAD_LOCAL_RESPONSE.set(response);
	}

	public HttpServletRequest getRequest() {
		return THREAD_LOCAL_REQUEST.get();
	}

	public HttpServletResponse getResponse() {
		return THREAD_LOCAL_RESPONSE.get();
	}

	/**
	 * 获取登录用户信息
	 * 
	 * @return
	 */
	public LoginUser getLoginUser() {
	    String text = (String) getRequest().getAttribute(Const.LOGIN_USER_KEY);
	    if(StringUtils.isBlank(text)) {
	        return null;
	    }
		return JSON.parseObject(text, LoginUser.class);
	}

	/**
	 * 是否是超级管理员
	 * 
	 * @return
	 */
	public boolean isAdminUser() {
		return false;
	}

	/**
	 * 是否是项目管理员
	 * 
	 * @param pid 项目ID
	 * @return
	 */
	public boolean isPM(Long pid) {
		return false;
	}

	/**
	 * 是否是项目的成员
	 * 
	 * @param pid 项目ID
	 * @return
	 */
	public boolean isMember(Long pid) {
		return true;
	}

	/**获取根路径**/
	public String getBasePath() {
		HttpServletRequest request = getRequest();
		return request.getAttribute(Const.BASE_PATH).toString();
	}

	protected String toJson(Object obj) {
		return JSONObject.toJSONString(obj);
	}

	public JobInfo initJobInfo(JobInfoForm form){
	    
	    JobInfo info = new JobInfo();
	    info.setId(form.getId());
	    info.setUserId(getLoginUser().getId());
	    info.setPid(form.getPid());
	    info.setJobName(form.getJobName());
	    info.setNote(form.getNote());

		//from
		info.setFromId(form.getFromId());

		//to
		info.setToId(form.getToId());
		info.setToType(form.getToType());

		info.setCronExpression(form.getCronExpression());
		info.setPeriodType(form.getPeriodType());

		return info;
	}

	public PropertiesExt getI18n(){
		PropertiesExt i18n = (PropertiesExt) getRequest().getAttribute(Const.I18N);
		i18n = i18n==null?I18nMessages.I18N.get(Locale.US.getLanguage()):i18n;
		return i18n;
	}
	
	public String getI18nMsg(String msgId, Object ...args){
		if(StringUtils.isBlank(msgId)){
			return "";
		}
		String msg = getI18n().getProperty(msgId);
		if(msg == null){
			msg = "";
		}
		if(args != null && args.length!=0){
			try {
				msg = String.format(msg, args);
			} catch (Exception e) {
				LOGGER.error("massage msgId={}", msgId);
			}
		}
		return msg;
	}
	/***
	 * i18n格式化提示信息
	 * @param res
	 */
	public Result formatMsg(Result res){
		if(res!=null && res.getCode()!=SUCESS){
			ResultMsg i18nMsg = res.getI18nMsg();
			// 1.title= 取":"前面, 不含":"取全部
			// 2.detail=取":"后面, 不含":"取空
			// 3.msg=错误信息, 为空取detail
			String i18nStr = res.getMsg();
			if(i18nMsg==null){
				i18nMsg = StringUtils.isBlank(i18nStr)?new ResultMsg(MSGT0000E000000):new ResultMsg();
				i18nStr = StringUtils.isBlank(i18nStr)?this.getI18nMsg(i18nMsg.getKey(), i18nMsg.getArgs()):i18nStr;
			} else {
				i18nStr = this.getI18nMsg(i18nMsg.getKey(), i18nMsg.getArgs());
			}
			
			String sep = ":";
			if(StringUtils.isNotBlank(i18nStr)){
				if(i18nStr.contains(sep)){
					String title = i18nStr.substring(0, i18nStr.indexOf(sep));
					String detail= i18nStr.substring(i18nStr.indexOf(sep)+sep.length());
					i18nMsg.setTitle(title.trim());
					i18nMsg.setDetail(detail.trim());
				} else {
					i18nMsg.setTitle(i18nStr.trim());
					i18nMsg.setDetail("");
				}
				
				if(StringUtils.isBlank(res.getMsg())){
					res.setMsg(i18nMsg.getDetail());
				}
			}
			
			res.setI18nMsg(i18nMsg);
		}
		return res;
	}
}
