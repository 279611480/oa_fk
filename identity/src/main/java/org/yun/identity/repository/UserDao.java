package org.yun.identity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yun.identity.domain.User;

public interface UserDao extends JpaRepository<User, String> {

	User findByLoginName(String loginName);
	
	//会自动把查询条件前后使用%包起来，使用like（模糊）查询
	//where name like ?1
	Page<User> findByNameContaining(String keyword, Pageable pageable);
	
}
