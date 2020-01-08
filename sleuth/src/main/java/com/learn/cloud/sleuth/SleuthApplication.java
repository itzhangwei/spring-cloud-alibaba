package com.learn.cloud.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title SleuthApplication
 * @package com.learn.cloud.sleuth
 * @description 日志追踪收集服务
 * @date 2020/1/6 3:32 下午
 */
@SpringBootApplication
@EnableZipkinServer
public class SleuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(SleuthApplication.class, args);
	}
}
