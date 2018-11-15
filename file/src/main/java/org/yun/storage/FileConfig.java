package org.yun.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("org.yun")
public class FileConfig implements WebMvcConfigurer {
	
	public static void main(String[] args) {
		SpringApplication.run(FileConfig.class, args);
	}

}
