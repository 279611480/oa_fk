package org.yun.leave.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.yun.identity.domain.User;

@Entity
@Table(name="leave_leave")
public class Leave implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@GeneratedValue(generator="uuid2")
	@Column(length=36)
	private String id;
	
	//请假类型

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	private LeaveType type;


	//请假人
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="leave_user_id")
	private User user;
	
	//请假时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date LeavaTime;
		
	//工资
	private Double salary;//
		
	//请假状态
	@Enumerated(EnumType.STRING)
	private Status status;
	
	//请假理由
	@Lob
	private String reason;
	
	/**枚举请假的状态  被上面调用*/
	public static enum Status{
		/**
		 *已用
		 * */
		USED,
		/**
		 * 未用
		 * */
		UNUSED;
	}
	
	
	
	public LeaveType getType() {
		return type;
	}
	public void setType(LeaveType type) {
		this.type = type;
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
	public Date getLeavaTime() {
		return LeavaTime;
	}
	public void setLeavaTime(Date leavaTime) {
		LeavaTime = leavaTime;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
	
	
}
