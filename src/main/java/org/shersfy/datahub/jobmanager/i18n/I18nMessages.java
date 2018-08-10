package org.shersfy.datahub.jobmanager.i18n;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.shersfy.datahub.commons.beans.ResultMsg;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.commons.utils.LocaleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.support.ServletContextResource;


/**
 * 加载国际化文件<br/>
 * 文件名规则: xxx_Locale.properties, 如messages_en_US.properties
 */
public class I18nMessages implements I18nCodes{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(I18nMessages.class);
	
	public static ResultMsg getI18nMsg(String error, Object...args){
		return getI18nMsg(null, new Exception(error), args);
	}
	
	public static String getCauseMsg(Throwable ex){
		return DatahubException.getCauseMsg(ex);
	}
	/**
	 * 创建ResultMsg对象
	 * 
	 * @return ResultMsg
	 */
	public static ResultMsg getI18nMsg(String defaultKey, Throwable ex, Object...args){
		
		String error = getCauseMsg(ex);
		
		ResultMsg msg = new ResultMsg();
		// MSGT0000E000000=系统错误
		defaultKey = StringUtils.isBlank(defaultKey)?MSGT0000E000000:defaultKey;
		String search = null;
		
		// start 判断异常类型
		if(ex instanceof UnknownHostException){
		}
		// end	   判断异常类型
		
		// start 判断Message类型
		else if(error.contains("hive url upgrade")){
			defaultKey = MSGT0002E000001;
		}
		else if(error.contains("AccessControlException") && error.contains("Client cannot authenticate via:[TOKEN, KERBEROS]")){
			defaultKey = MSGT0002E000002;
		}
		else if(error.contains("file not exists")){
			search 	= "file not exists";
			defaultKey 	= MSGT0019E000001;
		}
		else if(error.contains("UnknownHostException")){
			search 	= "UnknownHostException:";
			defaultKey 	= MSGT0000E000002;
		}
		else if(error.contains("invalid JDBC url format")){
			String str1 = "invalid JDBC url format";
			String str2 = "connect to";
			defaultKey 	= MSGT0003E000006;
			args = new String[]{error.substring(error.indexOf(str1)+str1.length(), error.indexOf(str2)).trim(),
					error.substring(error.indexOf(str2)+str2.length()).trim()};
		}
		else if (error.contains("通过端口")) {
			defaultKey     = MSGT0003E000004;
			args = new String[] {error.substring(error.indexOf("通过端口")+"通过端口".length(), error.indexOf("连接到主机")-1),
					error.substring(error.indexOf("连接到主机")+"连接到主机".length(), error.indexOf("的 TCP")-1)};
		}
//		else if (error.startsWith("Login failure for") && error.contains("from keytab")) {
//			defaultKey  = "form.info.000033;
//		}
//		else if (error.startsWith("Permission denied: user=")) {
//			defaultKey = "form.info.000034;
//			if (args != null && args.length > 0) {
//				args = new String[] {(String) args[1], error.substring(error.indexOf("user=")+5, error.indexOf(","))};
//			}
//		}
		// form.info.000050~... db error
		else if(error.contains("password mistake") || error.contains("Access denied for user")){
			defaultKey = MSGT0003E000002;
		}
		else if(error.contains("Unknown database")){
			defaultKey = MSGT0003E000003;
		}
		// end   判断Message类型
		
		if(search!=null){
			args = new String[]{error.substring(error.indexOf(search)+search.length())};
		}
		
		msg.setArgs(args);
		msg.setKey(defaultKey);
		return msg;
	}
	
	
	private Resource[] locations;
	
	private String fileEncoding;
	/**lang--messages**/
	public static Map<String, PropertiesExt> I18N = new ConcurrentHashMap<>();

	public I18nMessages() {}
	
	public I18nMessages(Resource[] locations) {
		super();
		this.locations = locations;
	}
	
	public static Properties getMessages(Locale locale){
		if(locale == null){
			locale = Locale.US;
		}
		return I18N.get(locale.getLanguage());
	}

	public Resource[] getLocations() {
		return locations;
	}

	public void setLocations(Resource... locations) throws IOException {
		for(Resource location :locations){
			if(location instanceof ServletContextResource){
				ServletContextResource scr = (ServletContextResource) location;
				String path = scr.getPath();
				String name = FilenameUtils.getName(path);
				if(name.endsWith(".properties")){
					//处理单个文件
					String lang = name.substring(name.indexOf("_")+1, name.indexOf("."));
					PropertiesExt pror = new PropertiesExt();
					PropertiesLoaderUtils.fillProperties(pror, new EncodedResource(scr, this.fileEncoding));
					I18N.put(LocaleUtil.toLocale(lang).getLanguage(), pror);
					File file = location.getFile();
					LOGGER.info("I18n loaded messages file {}", file.getName());
					
				}
				else{
					
				}
			}
		}
		
		this.locations = locations;
	}
	
	public void setLocation(Resource location) {
		this.locations = new Resource[] {location};
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

}
