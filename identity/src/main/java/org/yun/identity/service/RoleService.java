package org.yun.identity.service;

import java.util.List;

import org.yun.identity.domain.Role;

public interface RoleService {

	List<Role> findAllRoles();

	void save(Role role);

	void deleteById(String id);

	List<Role> findAllNoFixed();

	List<Role> findAll();
	
	
	
	
}
