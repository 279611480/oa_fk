package org.yun.identity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.yun.identity.domain.User;


public interface IdentityService {

	void save(User user);

	Page<User> findUsers(String keyword, Integer number);

	User findUserById(String id);

	void active(String id);

	void disable(String id);



	Optional<User> findByLoginName(String loginName);

	List<User> findUsers(String keyword);


	
	
	
	
	
}
