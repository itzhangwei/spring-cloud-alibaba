package com.learn.cloud.common.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title MyBaseMapper
 * @package com.learn.cloud.common.mybatis
 * @description 增加批量方法
 * @date 2020/1/2 4:22 下午
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
	
	/**
	 * 批量插入 <BR>
	 *     而mp默认仅支持 list, collection, array 3个命名，不然无法自动填充
	 * @param batchList 批量插入实体类
	 * @return int
	 * @author zhangwei
	 * @createTime 2020/1/2 4:24 下午
	 */
	int myInsertBatch(@Param("list") List<T> batchList);
	
}
