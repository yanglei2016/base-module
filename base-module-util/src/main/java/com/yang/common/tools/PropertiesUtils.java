package com.yang.common.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * properties文件工具类
 * @author yanglei
 * 2017年7月14日 上午9:55:38
 */
public class PropertiesUtils extends PropertyPlaceholderConfigurer {
	
	private static Map<String, String> propsMap = null; //可以考虑放入redis缓存
	
	/**
	 * 读取配置
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		
		propsMap = new HashMap<String, String>();
		for(Object key : props.keySet()){
			propsMap.put(key.toString(), props.getProperty(key.toString()));
		}
	}
	
	/**
	 * 获取配置信息
	 * @param key
	 * @return
	 * @author yanglei
	 * 2017年7月14日 上午10:20:30
	 */
	public static String getProp(String key){
		return propsMap.get(key);
	}
}
