package org.yun.security;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yun.security.interceptors.UserHolderInterceptors;


@SpringBootApplication//使用SpringBoot自带的Tomcate
@EnableJpaRepositories//激活Jpa的自动Dao扫描
@ComponentScan("org.yun")//包扫描
/**实现WebMvcConfigurer   增加拦截器 不是这里想要的*/
//WebSecurityConfigurerAdapter implements WebMvcConfigurer
public class SecurityConfig extends WebSecurityConfigurerAdapter   implements WebMvcConfigurer {
	
	@Override/**注册一个拦截器，拦截所有的路径，拿到线程里面的user信息*/
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserHolderInterceptors())
				.addPathPatterns("/**")//表示会去找下一级（目录/菜单等）
				//默认Spring Security的拦截器，已经在其他拦截器之前
				//所以不用使用order也是可以有效的
				//如果不能正常获取到User(通过UserHolder)，那么就需要修改顺序
				//.order(Integer.MAX_VALUE);//升序排（ 1 2 3 4 5 ）  拿到大的值   
										  //表示在所有的拦截器之前，就会把线程里面的user信息拿到
				;
	}
	
	
	
	
	//基于Http的安全控制
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()//验证请求
		//登录页面的地址和其他的静态页面都不要权限
		// /*表示目录下的任何地址，但是不包括子目录
		// /**则是连子目录都一起匹配
		.antMatchers("/security/login", "/css/**", "/js/**", "/webjars/**", "/static/**")//
		.permitAll()// 不做访问判断
		.anyRequest()// 所有请求
		.authenticated()// 授权以后才能访问
		.and()// 并且
		.formLogin()// 使用表单进行登录
		.loginPage("/security/login")// 登录页面的位置，默认是/login
		// 此页面不需要有对应的JSP，而且也不需要有对应代码，只要URL
		// 这个URL是Spring Security使用的，用来接收请求参数、调用Spring Security的鉴权模块
		.loginProcessingUrl("/security/do-login")// 处理登录请求的URL
		
		//在登录成功以后，会判断Session里面是否有记录之前的URL，如果有则使用之前的URL继续访问
		//如果没有则使用defaultSuccessUrl
		//.defaultSuccessUrl("/index")//默认登录的页面
		
		.usernameParameter("loginName")// 登录名的参数名
		.passwordParameter("password")// 密码的参数名称
		.and().logout()//配置退出登录
		.logoutUrl("/security/do-logout")
		// .logoutSuccessUrl("/")
		// .and().httpBasic()// 也可以基于HTTP的标准验证方法（弹出对话框）
		.and().csrf()// 激活防跨站攻击功能
		;
		
	}



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
		registry.addViewController("/index").setViewName("security/index");
		//欢迎页，访问根目录重定向到一个首页
		registry.addRedirectViewController("/","/index");
		
	}

	public static void main(String[] args) {
		SpringApplication.run(SecurityConfig.class, args);//运行main方法   开启  TomCate  
	}
	
	
}
