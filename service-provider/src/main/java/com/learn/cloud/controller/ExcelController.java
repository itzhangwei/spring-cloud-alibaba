package com.learn.cloud.controller;

import com.alibaba.excel.EasyExcel;
import com.learn.cloud.common.entity.Customer;
import com.learn.cloud.common.listener.ExcelDataListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
	
	
	
	@PostMapping("/import")
	public String importExcel(MultipartFile file) throws IOException {
//		String filePath = "/Users/zack.zhang/Desktop/学习/learnImport.xlsx";
		EasyExcel.read(file.getInputStream(), Customer.class, new ExcelDataListener()).sheet().doRead();
		return "success";
	}
	
}
