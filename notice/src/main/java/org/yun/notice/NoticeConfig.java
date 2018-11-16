package org.yun.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan("org.yun")//包扫描
@EnableJpaRepositories//激活全自动扫面Dao层
public class NoticeConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(NoticeConfig.class, args);
	}
	
	
}