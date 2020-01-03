package com.learn.cloud.common.dao;

import com.learn.cloud.common.entity.Customer;
import com.learn.cloud.common.mybatis.MyBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ExcelDao
 * @package com.learn.cloud.common.util
 * @description excel数据导入数据库持久类
 * @date 2020/1/2 4:01 下午
 */
@Repository
public interface ExcelDao extends MyBaseMapper<Customer> {
}
