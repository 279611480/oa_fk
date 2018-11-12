package org.yun.security.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.yun.identity.domain.User;


public class UserDetails extends  org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**用户在数据里的id*/
	private String userId;
	/**数据库里面的用户的姓名*/
	private String name;
	
	/**
	 * 构造器  UserDetails
	 * 
	 * @param username  登录名
 	 * @param password	密码
	 * @param enabled	是否激活
	 * @param accountNonExpired	账户是否过期
	 * @param credentialsNonExpired	密码是否过期
	 * @param accountNonLocked	账户是否锁定
	 * @param authorities	集合、用户具有的角色、身份。我们在角色的时候有KEY,通常在KEY前面加上ROLE_即可
	 * 
	 * 
	 * */
	private UserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	
	/**
	 * 对应上面的信息  
	 * @param user数据库里面存储的User对象
	 * @param authorities集合，用户具有的角色、身份。我们在角色的时候有KEY，通常在KEY前面加上ROLE_即可
	 * 
	 * */
	public UserDetails(User user,Collection<? extends GrantedAuthority> authorities ) {
		super(user.getLoginName(),user.getPassword(),//
				user.getStatus() == User.Status.NORMAL,//用户正常（激活）
				user.getStatus() != User.Status.EXPIRED,//用户账户不过期
				user.getStatus() != User.Status.EXPIRED,//密码不过期
				user.getStatus() != User.Status.DISABLED,//不禁用
				authorities);
		this.userId = user.getId();
		this.name = user.getName();	
		
		
	}
	

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
