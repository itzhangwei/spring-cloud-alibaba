package com.learn.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ServiceProviderApplicaion
 * @package com.learn.cloud
 * @description 启动类
 * @date 2019/12/20 5:27 下午
 */
@SpringBootApplication(scanBasePackages = "com.learn.cloud.*")
public class ServiceProviderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceProviderApplication.class, args);
	}
}
