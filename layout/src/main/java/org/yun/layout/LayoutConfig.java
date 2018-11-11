package org.yun.layout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication//使用SpringBoot自带的Tomcate
@EnableJpaRepositories//激活Jpa的自动Dao扫描
@ComponentScan("org.yun")//包扫描
public class LayoutConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(LayoutConfig.class, args);//运行main方法   开启  TomCate  
	}
	
	
}
