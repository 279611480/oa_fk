package org.yun.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;


//实现此接口，自动就会需要实现UserDetilesService(要创建一个Bean  里面有着该有的信息)
public interface SecurityService extends UserDetailsService {
//查找用户的详细信息
	
	
	
}
