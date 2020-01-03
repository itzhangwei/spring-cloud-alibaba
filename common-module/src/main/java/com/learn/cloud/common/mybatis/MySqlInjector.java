package com.learn.cloud.common.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title MySqlInjector
 * @package com.learn.cloud.common.mybatis
 * @description 注册自定义方法
 * @date 2020/1/2 4:43 下午
 */
@Component
public class MySqlInjector extends DefaultSqlInjector {
	
	/**
	 * 如果只需增加方法，保留MP自带方法
	 * 可以super.getMethodList() 再add
	 * @return List<AbstractMethod>
	 */
	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
		List<AbstractMethod> methodList = super.getMethodList(mapperClass);
		methodList.add(new MyInsertBatch());
		return methodList;
	}
}
