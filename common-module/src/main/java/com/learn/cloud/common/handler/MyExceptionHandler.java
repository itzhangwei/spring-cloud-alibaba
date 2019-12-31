package com.learn.cloud.common.handler;

import com.alibaba.fastjson.JSON;
import com.learn.cloud.common.message.BaseException;
import com.learn.cloud.common.message.Errors;
import com.learn.cloud.common.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ExceptionHandler
 * @package com.learn.cloud.common.handler
 * @description 全局异常处理
 * @date 2019/12/30 6:31 下午
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {
	
	
	/**
	 * 全局异常处理，统一返回结构 <BR>
	 *
	 *     ExceptionHandler 定义拦截的异常，这里拦截所有异常
	 * @param e 拦截到的异常
	 * @return com.learn.cloud.common.response.ApiResult
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2019/12/30 6:37 下午
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ApiResult handler(Exception e){
		//判断是不是自定义异常
		if (e instanceof BaseException) {
			return ApiResult.error((BaseException) e);
		} else {
			BaseException sysError = Errors.SYS_ERROR;
			log.error("错误信息：",e);
			sysError.setDetailMsg(JSON.toJSONString(e));
			ApiResult error = ApiResult.error(sysError);
			error.setData(e);
			return error;
		}
	}
}
