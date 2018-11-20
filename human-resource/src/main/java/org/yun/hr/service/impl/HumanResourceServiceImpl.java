package org.yun.hr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yun.hr.domain.Department;
import org.yun.hr.domain.Employee;
import org.yun.hr.repository.DepartmentDao;
import org.yun.hr.repository.EmployeeDao;
import org.yun.hr.service.HumanResourceService;

@Service
public class HumanResourceServiceImpl implements HumanResourceService {
	
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private EmployeeDao employeeDao;
	
	
	@Override
	public void save(Department department) {
		//判断  有没有上级部门
		/**
		 * 如果有上级部门 且上级部门Id为NUll  那么，直接将当前部门的上级部门设置为Null
		 * 如果当前部门的Id为Null  那么，直接将当前部门 的id设置为null   说明没有上级部门
		 * */
		if(department.getParent() != null && StringUtils.isEmpty(department.getParent().getId())) {
			department.setParent(null);
		}
		if (StringUtils.isEmpty(department.getId())) {
			department.setId(null);
		}
		
		
		/**
		 * 01.同级部门，不能重名
		 * 拿到数据库里面有的部门
		 * 判断，如果有上级，那么调用持久层方法  根据传进去的上级部门，以及名字  查询出来
		 * 如果没有上级，那么根据传进去的上级部门查询出来
		 * 
		 * 如果说，有上级部门，但是上级部门id与当前部门的Id不同 表示部门的名字重复了
		 * */
		Department old;
		if(department.getParent() != null) {
			//有上级
			old = this.departmentDao.findByParentAndName(department.getParent(),department.getName());
		}else {
			//没有上级
			old = this.departmentDao.findByNameAndParentNull(department.getName());
		}
		
		if(old!=null && !old.getId().equals(department.getId())) {
			//Id不相同，表示重名了
			throw new IllegalArgumentException("部门的名称不能重复了");
		}
		/**
		 * 2.设置部门经理为当前部门的员工     因为必定是先是员工才是部门经理的
		 * 判断，如果当前部门的经理不为空 且 当前部门经理的用户不为空 且 当前部们经理用户 的id不为空
		 * 那么，拿到当前部门的部门经理（其实拿到的是员工），调用员工持久层方法根据当前员工的User拿到数据库有的记录
		 * 		判断，如果，数据库没有这个员工的信息，那么就意味着，当前用户还不是员工，直接调用员工持久层方法保存当前员工即可
		 * 			不然的话，说明，当前用户本来就是一名员工了  直接将从数据库里面查询得到的记录设置为员工即可
		 *不然，说明在页面，保存用户的时候，并没有选择，部门经理
		 *那么直接将当前部门的部门经理设置为空就可以了
		 * */
		if(department.getManager() != null
				&& department.getManager().getUser() != null
				&& !StringUtils.isEmpty(department.getManager().getUser().getId())) {
			Employee employee = department.getManager();//一个部门经理首先得是一个员工嘛
			Employee oldEmployee = this.employeeDao.findByUser(employee.getUser());//查找数据库，看看有没有这条记录
			if(oldEmployee == null) {
				//没有user对应的员工，意味着当前用户号不是员工
				//此时应该新增一个员工
				employee = this.employeeDao.save(employee);
			}else {
				employee = oldEmployee;
			}
			
			//将员工部门设置为当前部门
			employee.setDepartment(department);
			//将当前部门的经理设置为当前用户（员工）
			department.setManager(employee);
		}else {
			// 没有选择部门经理   【在页面保存用户的时候，就没有选择部门经理这个职位   所以直接设置为空就行了】
			// 在后面的其他业务处理的过程，如果一定需要部门经理处理，那么就要求使用独立的一个表来记录【委托】，把部门经理的职责委托给其他的用户
			// 委托的用户，不一定是当前部门的
			// 现在这里不处理委托
			department.setManager(null);
		}
		
		
		
	/** 3.排序的序号查询、处理               
	 *      从菜单处copy过来的*/
		if (old != null) {
			department.setNumber(old.getNumber());
		} else {
			Double maxNumber;
			if (department.getParent() == null) {
				maxNumber = this.departmentDao.findMaxNumberByParentNull();
			} else {
				maxNumber = this.departmentDao.findMaxNumberByParent(department.getParent());
			}
			if (maxNumber == null) {
				maxNumber = 0.0;
			}
			Double number = maxNumber + 10000000.0;
			department.setNumber(number);
		}
		this.departmentDao.save(department);
	}
	
}
