package org.yun.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.yun.menu.controller.MenuController;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("org.yun")//identity模块的包会自动被扫描   不使用排除检查
public class MenuConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(MenuConfig.class, args);
	}
}
