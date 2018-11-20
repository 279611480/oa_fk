package org.yun.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yun.hr.domain.Employee;
import org.yun.identity.domain.User;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String> {
	
	Employee findByUser(User user);

	Employee save(Employee employee);


	
	
	
}
