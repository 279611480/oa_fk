package org.yun.layout;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.devtools.remote.server.HttpStatusHandler;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication//使用SpringBoot自带的Tomcate
@EnableJpaRepositories//激活Jpa的自动Dao扫描
@ComponentScan("org.yun")//包扫描
/**实现WebMvcConfigurer   增加拦截器 不是这里想要的*/
public class LayoutConfig implements WebMvcConfigurer{
	
	/**拦截器**/
	@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// TODO Auto-generated method stub
			WebMvcConfigurer.super.addInterceptors(registry);
		}
	
	/**
	 * 在SpringBoot  里面增加自定义的过滤器
	 * 只需要Bean的类型为FilterRegistrationBean,那么就会自动作为一个过滤器增加到容器里面
	 * */
	
	@Bean
	public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter(){
		
//		ConfigurableSiteMeshFilter filter = new ConfigurableSiteMeshFilter() {
//		@Override
//		public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//				FilterChain filterChain) throws IOException, ServletException {
//			HttpServletRequest request = (HttpServletRequest) servletRequest;
//			System.out.println("过滤器被调用: " + request.getRequestURI());
//			super.doFilter(servletRequest, servletResponse, filterChain);
//		}
//	};
		/**此处需要这样增加过滤器**/
		ConfigurableSiteMeshFilter filter = new ConfigurableSiteMeshFilter();
		/**Filter的bean对象   见28注释*/
		FilterRegistrationBean<ConfigurableSiteMeshFilter> bean = new FilterRegistrationBean<>();
		/**
		 * 鼠标放在  ConfigurableSiteMeshFilter   参数上 可看见 过滤器该如何配置
		 * 这种过滤器的本质，是拦截器，Spring Boot里面，所有的静态文件都不会进入SpringMvc里面
		 * 所以跟本无法把	js,css拦截到
		 * */
		bean.addUrlPatterns("/*");/**拦截所有的URL    
		例如：之前在菜单里面添加的  各个菜单（用户页面  修改/添加页面 角色页面等）都会被拦截到*/          
		bean.setFilter(filter);/** 把过滤器添加到SpringBoot容器里面   将上面定义的Filter  添加到bean对象*/
		bean.setAsyncSupported(true);/**激活异步Servlet请求支持*/
		bean.setEnabled(true);/**激活使用*/
		/**设置，只处理浏览器的请求和错误的转发，Forward,Include等请求都不处理*/
		bean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
		
		/**初始化过滤器的参数*/
		Map<String,String> initParameters = new HashMap<>();//定义一个Map集合接收参数
		/**
		 *    使用main.jsp来装饰
		 *    /admin/*使用admin.jsp来装饰      	【配置不同的路径，使用不同的装饰器】
		 * */
		//initParameters.put("decoratorMappings", "/*=/WEB-INF/layouts/main.jsp\n/admin/*=/WEB-INF/layouts/admin.jsp\");");
			/**
			 * /*=/WEB-INF/layouts/main.jsp  ，表示所有的模块内容，公共可拥有的东西，
			 * 		也是把其他模块的页面，加进这里面来统一布局 
			 * 
			 * 			\n" 
						//不带横幅、菜单布局   
						+ "/security/login=/WEB-INF/layouts/simple.jsp"
			 * 将security模块的，界面，加入统一布局页面里面，在simple.jsp使用三个标签属性加入公告部分 
			 * */
			initParameters.put("decoratorMappings",
					//带横幅、菜单布局
					"/*=/WEB-INF/layouts/main.jsp\n" 
						//不带横幅、菜单布局   
						+ "/security/login=/WEB-INF/layouts/simple.jsp"
						/**将security模块的simple.jsp页面被main.jsp统一布局处理*/
					);
			// 排除某些路径不要装饰
			// initParameters.put("exclude", "/identity/role,/identity/role/*");
			
			//包含错误页面也一并装饰
			initParameters.put("includeErrorPages","true");//见ex.jsp字符编码格式设置处  2二行  开启页面错误设置
			
		bean.setInitParameters(initParameters);
		
		return bean;
	}
	
	/**配置自定义的错误页面
	 * 最简单的是在静态资源目录中，创建名为error的HTML文件
	* 如：400.html、4xx.html、500.html等。
	 *也可以在这个位置配置自定义的错误页面映射
	 * 最后也可以利用configureHandlerExceptionResolvers方法注册异常处理
	 * */
	@Bean
	public ErrorPageRegistrar errorPageRegistrar() {
		ErrorPageRegistrar errorPageRegistrar = new ErrorPageRegistrar() {
			@Override
			public void registerErrorPages(ErrorPageRegistry registry) {
				//registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/layout/ex"));   地址输错就能看到效果 404
				registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/layout/ex"));  //500不好看效果

			
			}
		};
		return errorPageRegistrar;
	}
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(LayoutConfig.class, args);//运行main方法   开启  TomCate  
	}
	
	
}
