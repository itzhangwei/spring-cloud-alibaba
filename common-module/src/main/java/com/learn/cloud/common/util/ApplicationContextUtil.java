package com.learn.cloud.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ApplicationContextUtil
 * @package com.learn.cloud.common.util
 * @description 获取applicationContext, 用于获取spring 中的 bean
 * @date 2020/1/2 7:12 下午
 *
 * ApplicationContextAware 在spring加载完成执行 setApplicationContext，并且提供 ApplicationContext参数对象。
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextUtil.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getContext(){
		return ApplicationContextUtil.applicationContext;
	}
	
}
