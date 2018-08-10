package com.lenovo.datahub.web.api;

import java.util.Date;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lenovo.datahub.bean.Result;
import com.lenovo.datahub.common.Const;
import com.lenovo.datahub.domain.ApiInfo;
import com.lenovo.datahub.domain.ApiRecord;
import com.lenovo.datahub.domain.BaseEntity;
import com.lenovo.datahub.domain.DbInfo;
import com.lenovo.datahub.domain.FtpInfo;
import com.lenovo.datahub.domain.HdfsInfo;
import com.lenovo.datahub.domain.S3Info;
import com.lenovo.datahub.domain.UserToken;
import com.lenovo.datahub.domain.vo.ProjectVo;
import com.lenovo.datahub.domain.vo.UserInfoVo;
import com.lenovo.datahub.service.ApiInfoService;
import com.lenovo.datahub.service.ApiRecordService;
import com.lenovo.datahub.service.BaseService;
import com.lenovo.datahub.service.UserTokenService;
import com.lenovo.datahub.utils.DateUtil;
import com.lenovo.datahub.web.api.form.APIForm;
import com.lenovo.datahub.web.controller.BaseController;

/**
 * API
 * @author PengYang
 * @date 2017-12-11
 *
 * @copyright Copyright Lenovo Corporation 2017 All Rights Reserved.
 */
public class APIController extends BaseController{

	@Resource
	private UserTokenService userTokenService;
	@Resource
	private ApiInfoService apiInfoService;
	@Resource
	private ApiRecordService apiRecordService;
	
	
	public Result check(APIForm form, BindingResult bundle) {
		bundle = form.check(bundle);
		if(bundle.hasErrors()){
			return errors(bundle.getAllErrors());
		}
		
		Result res = new Result();
		UserToken token = userTokenService.findByToken(form.getToken());
		
		// check
		// API.1001=token not exist
		if(token == null){
			res.setCode(TOKEN_NOT_EXIST);
			res.setMsg(getAPIMsg(res.getCode()));
			return res;
		}
		// API.1002=token disabled
		if(!token.getEnable()){
			res.setCode(TOKEN_DISABLED);
			res.setMsg(getAPIMsg(res.getCode()));
			return res;
		}
		// API.1003:token expired, deadline %s
		if(DateUtil.compareDate(new Date(), token.getExpiredTime())>0){
			String time = DateUtil.format(token.getExpiredTime(), Const.FORMAT_DATETIME);
			res.setCode(TOKEN_EXPIRED);
			res.setMsg(getAPIMsg(res.getCode(), time));
			return res;
		}
		// token is OK
		LoginUser user = userTokenService.decryptUserInfo(token.getUserInfo());
		form.setTokenUser(user);
		if(user == null){
			res.setCode(TOKEN_CREATOR_ERROR);
			res.setMsg(getAPIMsg(res.getCode()));
			return res;
		}
		
		Long pid = user.getPid();
		ProjectVo proj = getProjects(user).get(pid);
		if(proj==null){
			res.setCode(TOKEN_INVALID);
			res.setMsg(getAPIMsg(res.getCode(), token.getAppKey()));
			return res;
		}
		
		if(!token.getAppKey().equals(proj.getName())){
			proj.setProxyUser(user.getProject().getProxyUser());
			user.setProject(proj);
			String info = userTokenService.encryptToken(user).getUserInfo();
			
			token.setAppKey(proj.getName());
			token.setUserInfo(info);
			userTokenService.updateById(token);
		}
		
		String url = getRequest().getRequestURI();
		String ctx = getRequest().getContextPath();
		
		url = url.substring(ctx.length());
		
		ApiInfo api = apiInfoService.findByUrl(url);
		if(api == null){
			// API.1006=not opened the API [%s]
			res.setCode(TOKEN_NOT_OPENED_API);
			res.setMsg(getAPIMsg(res.getCode(), url));
			return res;
		}
		
		Date current = new Date();
		Long apiId   = api.getId();
		Long tokenId = token.getId();
		ApiRecord record = apiRecordService.findByApiIdAndTokenId(apiId, tokenId);
		if(record == null){
			record = new ApiRecord();
			record.setApiId(apiId);
			record.setTokenId(tokenId);
			record.setLastCallTime(current);
			record.setCreateTime(current);
			apiRecordService.insert(record);
		} else {
			Date last = record.getLastCallTime();
			long interval = (current.getTime() - last.getTime())/1000;
			if(api.getIntervals()-interval>0){
				// API.1007=continuous call api times too much, please wait for %s seconds retry
				res.setCode(1007);
				res.setMsg(getAPIMsg(res.getCode(), api.getIntervals()-interval));
				return res;
			}
			
			ApiRecord udp = new ApiRecord();
			udp.setId(record.getId());
			udp.setLastCallTime(current);
			apiRecordService.updateById(udp);
		}
		
		// 设置用户信息
		getRequest().setAttribute(Const.LOGIN_USER_KEY, user);
		
		LOGGER.info("url={}, app={}, params={}", url, token.getAppKey(), form);
		
		return res;
	}
	
	/**check对象id查询对象是否为null, check 是否为项目内资源**/
	protected Result checkNullAndPermission(BaseService<? extends BaseEntity, Long> service, 
			Long id, LoginUser tokenUser) {
		
		Result res = new Result();
		BaseEntity po = service.findById(id);
		if(po == null){
			res.setCode(FAIL);
			String info = service.getClass().getInterfaces()[0].getSimpleName().replace("Service", "");
			res.setMsg(getI18nMsg(MSGT0018I000001, info, id));
			return res;
		}
		
		boolean internal = false;
		boolean isTmpRes = true;
		if(po instanceof DbInfo){
			DbInfo obj = (DbInfo) po;
			internal = obj.getDelFlg() == BaseService.DEL;
			isTmpRes = obj.getDelFlg() == BaseService.TMP;
		} else if(po instanceof HdfsInfo){
			HdfsInfo obj = (HdfsInfo) po;
			internal = obj.getDelFlg() == BaseService.DEL;
			isTmpRes = obj.getDelFlg() == BaseService.TMP;
		} else if(po instanceof FtpInfo){
			FtpInfo obj = (FtpInfo) po;
			internal = obj.getDelFlg() == BaseService.DEL;
			isTmpRes = obj.getDelFlg() == BaseService.TMP;
		} else if(po instanceof S3Info){
			S3Info obj = (S3Info) po;
			internal = obj.getDelFlg() == BaseService.DEL;
			isTmpRes = obj.getDelFlg() == BaseService.TMP;
		}
		
		RequestMethod method = RequestMethod.valueOf(getRequest().getMethod());
		
		// 过滤1: 非内置资源 and 项目外资源
		boolean filter1 = !internal && po.getPid()!=tokenUser.getPid().longValue();
		// 过滤2: 修改非临时资源, 非GET and 非临时资源()
		boolean filter2 = method!=RequestMethod.GET && !isTmpRes;
		
		if(filter1 || filter2){
			res.setCode(TOKEN_PERMISSION_DENIED);
			res.setMsg(getAPIMsg(res.getCode()));
			LOGGER.error(res.getMsg());
			return res;
		}
		
		res.setModel(po);
		return res;
	}
	
	
	/**
	 * 获取API Message
	 * 
	 * @author PengYang
	 * @date 2017-12-13
	 * 
	 * @param code
	 * @param args
	 * @return
	 */
	public String getAPIMsg(Integer code, Object ...args){
		if(code==null){
			return "";
		}
		String msgId = "API."+code.toString();
		return getI18nMsg(msgId, args);
	}

	@Override
	public Result formatMsg(Result res) {
		
		if(res.getI18nMsg()!=null){
			String i18nmsg = getI18nMsg(res.getI18nMsg().getKey(), res.getI18nMsg().getArgs());
			StringBuilder buff = new StringBuilder(i18nmsg);
			if(StringUtils.isNotBlank(i18nmsg)){
				buff.append("\n");
				buff.append(res.getMsg());
				res.setMsg(buff.toString());
			}
			res.setI18nMsg(null);
		}
		
		return res;
	}
	
}
