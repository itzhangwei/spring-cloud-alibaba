package com.learn.cloud.common.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.learn.cloud.common.response.ApiResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ExceptionUtils
 * @package com.learn.cloud.common.util
 * @description Sentinel 限流处理
 * @date 2019/12/30 2:42 下午
 */
@Slf4j
public class ExceptionUtil {
	
	public static ApiResult handleException(BlockException ex) {
		log.info("服务限流了啊，exception：{}", ex == null ? "null" : ex.getMessage());
		
		return ApiResult.error("服务限流了啊");
	}
}
