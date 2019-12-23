package com.learn.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title HelloController
 * @package com.learn.cloud.controller
 * @description
 * @date 2019/12/20 5:29 下午
 */
@RestController
public class HelloController {
	
	@Value("${myself.hello}")
	private String hello;
	
	@GetMapping("/hello")
	public String hello() {
		return this.hello;
	}
}
