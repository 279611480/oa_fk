package org.yun.security.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yun.identity.UserHolder;
import org.yun.identity.domain.User;
import org.yun.security.domain.UserDetails;

/***
 * 配置拦截器   
 * 拿到线程里面user的信息
 * 再将user设置到线程里面去
 * */

public class UserHolderInterceptors extends HandlerInterceptorAdapter {
	
	@Override//重写方法
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			/**获取Spring Security里面保存的UserDetail信息（前面已经在这里面拿到了用户的信息）,
			 * 并且转换为User存储到UserHolder里面去（线程）
			 *   
			 *  获取当前用户信息的方式之一 			拿到UserDetails里面user对象
			 *   */
			//根据上下文去拿
			Object principai = SecurityContextHolder//通过这个类，调用方法  拿到UserDetails的user信息
								.getContext()//上下文
								.getAuthentication()//身份认证
								.getPrincipal();//（翻译是  委托人）	拿到这个对象    
												//下面将拿到的用户信息（UserDetails里面已经拿到了）设置到这个对象去
			//Spring Security在没用登录的时候，会把当前用户设置为【匿名用户】
			//anonymous
			if(principai instanceof UserDetails) {// instanceof XX的实例
				//将UserDetil的信息  设置到线程里面的User里面去   不然会把数据库的User信息修改掉
				UserDetails details = (UserDetails)principai;
				//线程的User   不会修改数据库的信息
				User user =new User();
				//使用UserHolder的方法  将拿到的用户信息，设置到线程里面去  
				user.setId(details.getUserId());
				user.setName(details.getName());
				
				//将信息设置到线程里面去
				UserHolder.set(user);
			}
			return true;
	}
	
	//清理现场 【每个拿完线程里面的信息后，都需要清理现场】   post请求 
 	@Override
 	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
 			ModelAndView modelAndView) throws Exception {
 		//清理现场
 		UserHolder.remove();
 	}
	
	
}
