package com.learn.cloud.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.learn.cloud.common.message.BaseException;
import com.learn.cloud.common.util.ApplicationContextUtil;
import com.learn.cloud.common.util.ClassScanner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
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
	private static final int BATCH_COUNT = 1;
	
	/**
	 * 存储数据list
	 */
	List<Serializable> list = new ArrayList<>();
	
	/**
	 * dao接口的包路径
	 */
	List<String> daoPathList = Collections.singletonList("com.learn.cloud.common.dao");
	
	/**
	 * 事物管理相关对象
	 */
	DataSourceTransactionManager transactionManager;
	
	TransactionDefinition transactionDefinition;
	
	TransactionStatus transaction;
	
	/**
	 * 原子布尔类，多线程环境下也可以保持现场安全, 无参数构造函数初始化 false。
	 *  这里其实没有多线程环境，如果存在多线程环境，上面的数据存放list也是不安全的。
	 *  由于这里每次读取excel的时候，监听对象都是new的，所以没有多线程不安全的情况。
	 *  这里就当熟悉下 AtomicBoolean的使用吧。
	 */
	private AtomicBoolean isStartTransaction = new AtomicBoolean();
	
	/**
	 * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
	 *
	 */
	public ExcelDataListener() {
		
		
//		// 获取事物 如果事物放在构造函数里面也能达到目的，就是事物开启的太早，在做数据解析的时候就开启，而这个时候还没做保存操作。
//		transactionManager = ApplicationContextUtil.getContext().getBean(DataSourceTransactionManager.class);
//
//		transactionDefinition = ApplicationContextUtil.getContext().getBean(TransactionDefinition.class);
//
//		// 开启事物
//		transaction = transactionManager.getTransaction(transactionDefinition);
		
	}
	
	/**
	 * 这个每一条数据解析都会来调用
	 *
	 * @param data            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
	 * @param analysisContext 解析的数据对象
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
	 * 所有数据解析完成了 都会来调用,这个方法出现异常，不走 onException方法，
	 * 所以这里为了事物回滚，把里面的代码都try catch一下
	 *
	 * @param analysisContext 解析的数据对象
	 */
	@SneakyThrows
	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		try {
			// 这里也要保存数据，确保最后遗留的数据也存储到数据库
			saveData(analysisContext);
			int i = 1/0;
			// 提交事物
			log.info("所有数据解析完成！提交事物啦！");
			transactionManager.commit(transaction);
		} catch (Exception e) {
			this.onException(e, analysisContext);
		}
	}
	
	/**
	 *
	 * @param exception 异常
	 * @param context 解析对象
	 * @throws Exception
	 */
	@Override
	public void onException(Exception exception, AnalysisContext context) throws Exception {
		
		log.error("解析excel出现错误！回滚事物。", exception);
		
		// 事物回滚
		transactionManager.rollback(transaction);
		
		super.onException(exception, context);
	}
	
	/**
	 * 数据保存到数据库 <BR>
	 * @param analysisContext 解析的数据对象
	 * @return void
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2020/1/7 10:47 上午
	 */
	private void saveData(AnalysisContext analysisContext) {
		if (list.size() == 0) {
			return;
		}
		log.info("{}条数据，开始存储数据库！", list.size());
		// 获取目标对象字节码文件，从中获取bean
		Class<?> entityClass = analysisContext.readWorkbookHolder().getClazz();
		//todo 如何知道是哪个mapper,
		
		// 获取spring管理的mapper动态代理
		Class<?> mapperClass = this.gateMapperClass(entityClass);
		
		Object mapper = ApplicationContextUtil.getContext().getBean(mapperClass);
		
		try {
			//开启事物
			this.startTransaction();
			
			// 反射调用批量新增
			mapperClass.getMethod("myInsertBatch", List.class).invoke(mapper, this.list);
		}  catch (Exception e) {
			log.info("存储数据库失败！",e);
			throw new BaseException(e);
		}
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
	private Class<?> gateMapperClass(Class<?> entityClass) {
		Set<Class<?>> classSet = ClassScanner.scan(this.daoPathList);
		List<Class<?>> collect = classSet.stream().filter(c -> {
			if (!c.isInterface()) {
				return false;
			}
			// 获取父接口数组
			Type[] genericInterfaces = c.getGenericInterfaces();
			
			for (int i = 0; i < genericInterfaces.length; i++) {
				
				// 获取到父 interface 的 泛型 数组
				Type[] actualTypeArguments = ((ParameterizedTypeImpl) genericInterfaces[i]).getActualTypeArguments();
				
				for (int j = 0; j < actualTypeArguments.length; j++) {
					
					// 泛型type 强转换为class对象
					Class<?> cassTypeArgument = (Class<?>) actualTypeArguments[i];
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
	
	/**
	 * 方法描述,必填 <BR>
	 * @param
	 * @author zhangwei
	 * @createTime 2020/1/7 11:25 上午
	 */
	public void startTransaction(){
		/**
		 * compareAndSet 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
		 * expect - 预期值
		 * update - 新值
		 * return 如果成功，则返回 true。返回 False 指示实际值与预期值不相等
		 */
		if (this.isStartTransaction.compareAndSet(false, true)) {
			log.debug("开启事物管理！");
			// 事物还没有开启，这里开启事物
			// 获取事物
			transactionManager = ApplicationContextUtil.getContext().getBean(DataSourceTransactionManager.class);
			
			transactionDefinition = ApplicationContextUtil.getContext().getBean(TransactionDefinition.class);
			
			// 开启事物
			transaction = transactionManager.getTransaction(transactionDefinition);
		}
		
	}
}
