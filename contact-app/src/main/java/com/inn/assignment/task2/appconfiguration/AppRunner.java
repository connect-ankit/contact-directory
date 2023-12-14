package com.inn.assignment.task2.appconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.inn" })
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = { "com.inn" })
@EnableJpaRepositories(basePackages = "com.inn")
@EntityScan("com.inn")
public class AppRunner {

	public static void main(String[] args) {
		SpringApplication.run(AppRunner.class, args);

	}

}
