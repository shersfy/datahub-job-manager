package org.shersfy.datahub.jobmanager.i18n;

import java.util.Properties;

public class PropertiesExt extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getProperty(String key, String arg) {
		String value = getProperty(key);
		if(value!=null){
			value = String.format(value, arg);
		}
		return value;
	}
	
	/**
	 * 带参数
	 * 
	 * @param key
	 * @param args
	 * @return String
	 */
	public String getProperty(String key, Object ... args) {
		String value = getProperty(key);
		if(value!=null){
			value = String.format(value, args);
		}
		return value;
	}
	

}
