package org.yun.identity.service.impl;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.yun.identity.domain.Role;
import org.yun.identity.repository.RoleDao;
import org.yun.identity.service.RoleService;

@Service
public class RoleServiceImpl  implements RoleService,InitializingBean  {

	@Autowired
	private RoleDao roleDao;
	/**
	 * 初始化要做的事   实现接口 写方法
	 * 在服务器启动的时候，检查是否有预置的角色，如果没有则自动加上
	 * */
	@Override
	@Transactional
	public void afterPropertiesSet() throws Exception {
		// 在服务器启动的时候，检查是否有预置的角色，如果没有则自动加上
		Role admin = this.roleDao.findByRoleKey("ADMIN")//需要手动的加给用户
				.orElse(new Role());
		admin.setName("超级管理员");
		admin.setRoleKey("ADMIN");
		this.save(admin);
		
		//所有用户的默认角色
		//根据roleKey查找
		Role user = this.roleDao.findByRoleKey("USER")//
				//如果没有查询到则新创建一个  调用方法.orElse()
				.orElse(new Role());
		user.setName("普通用户");
		user.setRoleKey("USER");
		user.setFixed(true);
		this.save(user);
	}

	
	
	
	
	@Override
	public List<Role> findAllRoles() {
		return this.roleDao.findAll();
	}
	
	@Override
	public void save(Role role) {
		if(StringUtils.isEmpty(role.getId())) {
			role.setId(null);
		}
		//调用持久层方法，根据角色主键，查询数据
		Role old = roleDao.findByRoleKey(role.getRoleKey())
				//如果没找到，返回null
				.orElse(null);

		if(		//如果id没有，表示是新增的情况，old为空，表示数据库没有重复的数据
				role.getId() == null && old == null
				//有id则表示修改的情况，old为空，表示与数据库的数据不重复，
				//且角色获得的id与数据库查询的id不同，那么就是说是  修改的情况
				||role.getId() !=null && old != null && role.getId().equals(old.getId())
				//有id则表示修改的情况，old为空，表示与数据库的数据不重复，
				|| role.getId() != null && old==null
				) {
			roleDao.save(role);
			
		}else {
			throw new IllegalArgumentException("角色的key是唯一的，不能重复");
		}
	}

	@Override
	public void deleteById(String id) {
		roleDao.deleteById(id);
		
	}

	@Override
	public List<Role> findAllNoFixed() {
		return this.roleDao.findByFixedFalseOrderByName();
	}

	@Override
	public List<Role> findAll() {

		return this.roleDao.findAll();
	}

}
