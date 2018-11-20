package org.yun.hr.service;

import org.yun.hr.domain.Department;

public interface HumanResourceService {
	/**
	 * 同级部门中，不能有同名的部门（跟菜单一样）
	 * 
	 * @param department
	 */
	void save(Department department);
	
	
	
}
