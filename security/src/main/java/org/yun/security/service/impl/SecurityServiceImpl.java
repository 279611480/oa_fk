package org.yun.security.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yun.identity.domain.User;
import org.yun.identity.service.IdentityService;
import org.yun.security.domain.UserDetails;
import org.yun.security.service.SecurityService;


//Spring Security发现内存中有实现了SecurityService的实例，就不会自动创建  不然每次都创建一个，就很糟糕了
@Service
public class SecurityServiceImpl implements SecurityService  {
	
	@Autowired//调用用户模块的服务层方法
	private IdentityService identityService;
	
	@Override
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
		Optional<User> optional = identityService.findByLoginName(loginName);
		User user = optional.orElseThrow(() -> { //
			return new UsernameNotFoundException("用户的登录名 " + loginName + " 没有对应的用户信息！");//
		});
		Collection<GrantedAuthority> authorities = new HashSet<>();
		// 获取所有的角色，在角色的KEY前面加上ROLE_开头作为【已授权的身份】
		// ROLE_是Spring Security要求的
		user.getRoles().forEach(role -> {
			GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_" + role.getRoleKey());
			authorities.add(ga);
		});
		UserDetails ud = new UserDetails(user, authorities);

		return ud;
	}
}
