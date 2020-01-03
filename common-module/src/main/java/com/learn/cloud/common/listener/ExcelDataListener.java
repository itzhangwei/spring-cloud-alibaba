package com.learn.cloud.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.learn.cloud.common.message.BaseException;
import com.learn.cloud.common.util.ApplicationContextUtil;
import com.learn.cloud.common.util.ClassScanner;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ExcelDataListener
 * @package com.learn.cloud.common.listener
 * @description execl 数据监听器
 * @date 2020/1/2 3:54 下午
 * <p>
 * https://alibaba-easyexcel.github.io/quickstart/read.html
 */
@Slf4j
public class ExcelDataListener extends AnalysisEventListener<Serializable> {
	/**
	 * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
	 */
	private static final int BATCH_COUNT = 3000;
	
	List<Serializable> list = new ArrayList<Serializable>();
	
	
	/**
	 * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
	 *
	 * @param excelDao
	 */
	public ExcelDataListener() {
	}
	
	/**
	 * 这个每一条数据解析都会来调用
	 *
	 * @param data            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
	 * @param analysisContext
	 */
	@Override
	public void invoke(Serializable data, AnalysisContext analysisContext) {
		log.info("解析到一条数据:{}", JSON.toJSONString(data));
		list.add(data);
		// 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
		if (list.size() >= BATCH_COUNT) {
			saveData(analysisContext);
			// 存储完成清理 list
			list.clear();
		}
	}
	
	/**
	 * 所有数据解析完成了 都会来调用
	 *
	 * @param analysisContext
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		// 这里也要保存数据，确保最后遗留的数据也存储到数据库
		saveData(analysisContext);
		log.info("所有数据解析完成！");
	}
	
	/**
	 * 加上存储数据库
	 */
	private void saveData(AnalysisContext analysisContext) {
		log.info("{}条数据，开始存储数据库！", list.size());
		// 获取目标对象字节码文件，从中获取bean
		Class entityClass = analysisContext.readWorkbookHolder().getClazz();
		//todo 如何知道是哪个mapper,
		
		// 获取spring管理的mapper动态代理
		Class mapperClass = this.gateMapperClass(entityClass);
		
		Object mapper = ApplicationContextUtil.getContext().getBean(mapperClass);
		
		try {
			// 反射调用批量新增
			mapperClass.getMethod("myInsertBatch", List.class).invoke(mapper, this.list);
		}  catch (Exception e) {
			log.info("存储数据库失败！");
			e.printStackTrace();
		}

		log.info("存储数据库成功！");
	}
	
	/**
	 * 获取本次excel读取的实体对象对应的的数据表的mapper字节class对象<BR>
	 *     通过扫指定的dao下面的mapper对象，获取所有的mapper字节对象set集合，
	 *     然后和本次的excel读取的对象字节对象比较，如果mapper头上的泛型是本次读取的实体类型，
	 *     获取此mapper字节对象返回。
	 * @param entityClass 本次excel读取的实体对象
	 * @return java.lang.Class
	 * @author zhangwei
	 * @createTime 2020/1/3 3:35 下午
	 */
	private Class gateMapperClass(Class entityClass) {
		Set<Class> classSet = ClassScanner.scan(Arrays.asList("com.learn.cloud.common.dao"));
		List<Class> collect = classSet.stream().filter(c -> {
			if (!c.isInterface()) {
				return false;
			}
			// 获取父接口数组
			Type[] genericInterfaces = c.getGenericInterfaces();
			
			for (int i = 0; i < genericInterfaces.length; i++) {
				
				// 获取到父 insterface 的 泛型 数组
				Type[] actualTypeArguments = ((ParameterizedTypeImpl) genericInterfaces[i]).getActualTypeArguments();
				
				for (int j = 0; j < actualTypeArguments.length; j++) {
					
					// 泛型type 强转换为class对象
					Class cassTypeArgument = (Class) actualTypeArguments[i];
					if (cassTypeArgument.getName().equals(entityClass.getName())) {
						
						//找到了mapper 的泛型是本次读取的excel的对象类型
						return true;
					}
				}
				
			}
			return false;
		}).collect(Collectors.toList());
		if (collect.size() == 0) {
			throw new BaseException("没有找到对应数据表");
		}
		if (collect.size() > 1) {
			throw new BaseException("找到多个对应数据表");
		}
		// 获取spring生成的mapper代理类
		return collect.get(0);
	}
}
