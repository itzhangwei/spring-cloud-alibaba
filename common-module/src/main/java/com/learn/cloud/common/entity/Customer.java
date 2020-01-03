package com.learn.cloud.common.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title Customer
 * @package com.learn.cloud.common.entity
 * @description
 * @date 2020/1/2 4:59 下午
 */
@Data
@TableName("t_customer")
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 877442816216363500L;
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	
	/**
	 * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
	 */
	@ExcelProperty("姓名")
	private String name;
	
	@ExcelProperty("年龄")
	private Integer age;
	
	@ExcelProperty("性别")
	private String sex;
	
	@ExcelProperty("出生日期")
	private Date birthday;
	
	@ExcelProperty("备注")
	private String remarks;
}
