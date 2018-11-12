package org.yun.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication//使用SpringBoot自带的Tomcate
@EnableJpaRepositories//激活Jpa的自动Dao扫描
@ComponentScan("org.yun")//包扫描
/**实现WebMvcConfigurer   增加拦截器 不是这里想要的*/
public class SecurityConfig implements WebMvcConfigurer{
	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//系统默认自动把静态文件的根目录，映射到/public   /static   /resources里面 	
	}

	
	/***
	 * 当控制器，只有一行代码，用于返回一个页面的时候，不需要写控制器
	 * 直接注册一个【资源处理器】即可
	 * */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//动态注册URL和视图的映射关闭，解决控制器里面几乎没有代码的问题
		registry.addViewController("/security/login")//接收浏览器（URL）传过来的请求
								.setViewName("security/login");//找到对应的资源位置
		
	}

	public static void main(String[] args) {
		SpringApplication.run(SecurityConfig.class, args);//运行main方法   开启  TomCate  
	}
	
	
}
