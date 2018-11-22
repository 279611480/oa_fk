package org.yun.hr.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "hr_department")
public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 跟菜单通用的属性，可以写成一个Tree类作为通用父类，用继承映射
	@Id
	@Column(length = 36)
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String id;//部门id
	private String name;//部门名字
	 
	 /**
	 * 每个部门都可能有相同的子部门  所以多对一  此处管理关系
	 * */
	@ManyToOne
	@JoinColumn(name="parent_id")
	@JsonIgnore/**防止数据库死循环   所以使用这个注解*/
	private Department parent;
	
	/**
	 * 子部门   一对多   使用number排序
	 * */
	@OneToMany(mappedBy="parent")
	@JsonProperty("children")
	@OrderBy("number")
	private List<Department> childs; 
	
	
	private Double number; 
	
	
	/**
	 * 经理，必须是与员工，
	 * 员工必须是用户  
	 * 一对一
	 * 纯粹创建一个部门经理  
	 * */
	@OneToOne
	@JoinColumn(name="manget_id")
	private Employee manager;

	
	/**
	 * 部门里面的员工  关系由员工处理   一对多
	 * 防止死循环  使用  @JsonIgnore注解  忽略
	 * */		 
	@OneToMany(mappedBy = "department")
	@JsonIgnore
	private List<Employee> employees;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Department getParent() {
		return parent;
	}


	public void setParent(Department parent) {
		this.parent = parent;
	}


	public List<Department> getChilds() {
		return childs;
	}


	public void setChilds(List<Department> childs) {
		this.childs = childs;
	}


	public Double getNumber() {
		return number;
	}


	public void setNumber(Double number) {
		this.number = number;
	}


	public Employee getManager() {
		return manager;
	}


	public void setManager(Employee manager) {
		this.manager = manager;
	}


	public List<Employee> getEmployees() {
		return employees;
	}


	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
		
	
}
