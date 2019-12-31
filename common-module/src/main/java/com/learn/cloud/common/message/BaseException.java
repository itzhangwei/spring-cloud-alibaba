package com.learn.cloud.common.message;

import lombok.Data;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title BaseErrors
 * @package com.learn.cloud.common.message
 * @description 自定义异常
 * @date 2019/12/30 3:56 下午
 */
@Data
public class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = 7533771666280658218L;
	
	/**
	 * 简要错误信息
	 */
	private String message;
	
	/**
	 * 失败code码
	 */
	private final String code = "1";
	
	/**
	 * 错误类别
	 */
	private String errorType ;
	
	/**
	 * 详细错误信息
	 */
	private String detailMsg ;
	
	public BaseException(){
		super();
	}
	
	public BaseException(String errorType, String message) {
		super(message);
		this.message=message;
		
		this.errorType=errorType;
	}
	
	
	public BaseException(String message) {
		super(message);
		this.message = message;
	}
	
}
