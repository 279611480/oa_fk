package org.yun.hr.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.yun.identity.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="hr_employee")
public class Employee {
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	
	/**
	 * 员工关联的用户   一对一
	 * */

	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 部门表 员工字段一对多   对应这里的多对一   员工的归属部门
	 * */
	@ManyToOne
	@JoinColumn(name="department_id")
	@JsonIgnore//为了忽略死循环
	private Department department;
	
	
	/**
	 * 入职时间
	 * */
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinTime;
	
	/**
	 * 员工状态
	 * */
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public static enum Status{
		NORMAL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	
}
