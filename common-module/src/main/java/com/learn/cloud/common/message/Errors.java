package com.learn.cloud.common.message;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title Errors
 * @package com.learn.cloud.common.message
 * @description 错误类型
 * @date 2019/12/30 3:51 下午
 */
public interface Errors {
	
	
	BaseException SYS_ERROR = new BaseException("-9", "系统错误");
	
	/** 调用不存在的服务请求:{0} */
	BaseException NO_API = new BaseException("1", "不存在的服务请求");
	/** 服务请求({0})的参数非法 */
	BaseException ERROR_PARAM = new BaseException("2", "参数非法");
	/** 服务请求({0})缺少应用键参数:{1} */
	BaseException NO_APP_ID = new BaseException("3", "缺少应用键参数");
	/** 服务请求({0})的应用键参数{1}无效 */
	BaseException ERROR_APP_ID = new BaseException("4", "应用键参数无效");
	/** 服务请求({0})需要签名,缺少签名参数:{1} */
	BaseException NO_SIGN_PARAM = new BaseException("5", "缺少签名参数");
	/** 服务请求({0})的签名无效 */
	BaseException ERROR_SIGN = new BaseException("6", "签名无效");
	/** 请求超时 */
	BaseException TIMEOUT = new BaseException("7", "请求超时");
	/** 服务请求({0})业务逻辑出错 */
	BaseException ERROR_BUSI = new BaseException("8", "业务逻辑出错");
	/** 服务不可用 */
	BaseException SERVICE_INVALID = new BaseException("9", "服务不可用");
	/** 请求时间格式错误 */
	BaseException TIME_INVALID = new BaseException("10", "请求时间格式错误");
	/** 序列化格式不存在 */
	BaseException NO_FORMATTER = new BaseException("11", "序列化格式不存在");
	/** 不支持contectType */
	BaseException NO_CONTECT_TYPE_SUPPORT = new BaseException("12", "不支持contectType");
	/** json格式错误 */
	BaseException ERROR_JSON_DATA = new BaseException("13", "json格式错误");
	/** accessToken错误 */
	BaseException ERROR_ACCESS_TOKEN = new BaseException("14", "accessToken错误");
	/** accessToken expired */
	BaseException EXPIRED_ACCESS_TOKEN = new BaseException("15", "accessToken expired");
	/** accessToken not found */
	BaseException UNSET_ACCESS_TOKEN = new BaseException("16", "accessToken not found");
	/** jwt操作失败 */
	BaseException ERROR_OPT_JWT = new BaseException("17", "jwt操作失败");
	/** jwt错误 */
	BaseException ERROR_JWT = new BaseException("18", "jwt错误");
	/** 加密算法不支持 */
	BaseException ERROR_ALGORITHM = new BaseException("19", "加密算法不支持");
	/** ssl交互错误 */
	BaseException ERROR_SSL = new BaseException("20", "ssl交互错误");
	/** jwt过期 */
	BaseException ERROR_JWT_EXPIRED = new BaseException("21", "jwt过期");
	/** 文件上传错误 */
	BaseException ERROR_UPLOAD_FILE = new BaseException("22", "文件上传错误");
	/** 无访问权限 */
	BaseException NO_PERMISSION = new BaseException("23", "无访问权限");
	/** 新ssl交互不支持 */
	BaseException NEW_SSL_NOT_SUPPORTED = new BaseException("24", "新ssl交互不支持");
	
	/** 业务参数错误 */
	BaseException BUSI_PARAM_ERROR = new BaseException("100", "业务参数错误");
	
}
