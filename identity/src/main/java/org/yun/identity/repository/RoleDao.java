package org.yun.identity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yun.identity.domain.Role;



@Repository
public interface RoleDao extends JpaRepository<Role,String> {

	Optional<Role> findByRoleKey(String roleKey);

	List<Role> findByFixedTrue();

	List<Role> findByFixedFalseOrderByName();





	
}
