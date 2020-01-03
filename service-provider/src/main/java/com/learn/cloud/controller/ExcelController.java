package com.learn.cloud.controller;

import com.alibaba.excel.EasyExcel;
import com.learn.cloud.common.dao.ExcelDao;
import com.learn.cloud.common.entity.Customer;
import com.learn.cloud.common.listener.ExcelDataListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ExcelController
 * @package com.learn.cloud.controller
 * @description excel操作
 * @date 2020/1/2 5:11 下午
 */
@RestController
public class ExcelController {
	
	
	
	@GetMapping("/import")
	public String importExcel(){
		String filePath = "/Users/zack.zhang/Desktop/学习/learnImport.xlsx";
		EasyExcel.read(filePath, Customer.class, new ExcelDataListener()).sheet().doRead();
		return "success";
	}
	
}
