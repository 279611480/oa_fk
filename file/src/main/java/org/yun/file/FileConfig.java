package org.yun.file;

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

/*	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//动态注册URL和视图的映射关闭，解决控制器里面几乎没有代码的问题
		registry.addViewController("/file")//接收浏览器（URL）传过来的请求
								.setViewName("index");//找到对应的资源位置
		//欢迎页，访问根目录重定向到一个首页
		registry.addRedirectViewController("/","/index");	

	}*/

}
