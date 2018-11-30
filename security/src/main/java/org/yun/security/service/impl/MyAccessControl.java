package org.yun.security.service.impl;

import java.util.Set;

import javax.persistence.UniqueConstraint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

public class MyAccessControl {
	
	//定义日志记录
	private static final Logger LOG =  LoggerFactory.getLogger(MyAccessControl.class);
	
	//检查是否有权限
	public boolean check(Authentication authentication,HttpServletRequest request) {
		//拿到线程
		HttpSession session = request.getSession();
		//一直不会报错的异常
		@SuppressWarnings("unchecked")
		//拿到该用户有权利访问的所有url
		Set<String> urls = (Set<String>) session.getAttribute("urls");//前面securityConfig里面讲Url设置进去session里面了
		//拿到 请求【任何】的上下文
		String contextPath = request.getContextPath();
		//拿到该请求的url
		String requestUrl = request.getRequestURI();
		//判断  如果说拿到的请求的上下文 不是空的，那么将其截取掉   因为保存在数据库的URL都是没有上下文的
		if (!contextPath.isEmpty()) {
			//如果有contextPath需要截取掉，因为数据库里面记录的URL都没有contextPath
			requestUrl = requestUrl.substring(contextPath.length());//截取掉上下文的长度就可以了
		}
		//遍历循环之前拿到的  该用户可以访问的所有URL
		for(String url:urls) {
			//判断 如果说该url与请求的url相同 那么就返回true
			if(url.equals(requestUrl)) {
				return true;
			}
			
			//如果说，该url包含了正则表达式，那么也返回true
			if(requestUrl.matches(url)) {
				//匹配了正则表达式
				return true;
			}
			
			if(requestUrl.contains(url)) {
				return true;
			}
			
		}
		LOG.trace("访问被拒绝，访问Url:{},用户的Url集合",requestUrl,urls);
		
		return false;
	}
	
}
