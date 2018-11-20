package org.yun.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("org.yun")
public class HumanResourceConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(HumanResourceConfig.class, args);
	}
	
	
	
}
