package com.learn.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title HelloController
 * @package com.learn.cloud.controller
 * @description
 * @date 2019/12/20 5:29 下午
 */
@Slf4j
@RestController
public class HelloController {
	
	@Value("${myself.hello}")
	private String hello;
	
	private final DiscoveryClient discoveryClient;
	
	public HelloController(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}
	
	@GetMapping("/hello")
	public String hello() {
		return this.hello;
	}
	
	@GetMapping("/client")
	public List<JSONObject>  getClient(){
		log.debug("获取注册表中的注册信息");
		
		JSONObject server = new JSONObject();
		
		List<JSONObject> result = new ArrayList<>();
		// 获取注册列表
		List<String> services = this.discoveryClient.getServices();
		
		// 循环通过 instanceId 获取服务的详细信息
		services.forEach(id ->{
			
			// 相同的 id 可能有多个服务，集群
			List<ServiceInstance> instances = this.discoveryClient.getInstances(id);
			
			server.put("instanceId", id);
			
			server.put("instances", instances);
			result.add(server);
		});
		
		return result ;
	}
}
