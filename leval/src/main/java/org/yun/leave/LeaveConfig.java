package org.yun.leave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories//开启自动Dao扫描
@ComponentScan("org.yun")//包扫描
public class LeaveConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(LeaveConfig.class, args);
	}
	
}
