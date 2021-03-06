package org.yun.notice.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.yun.identity.domain.User;

//公告阅读表
@Entity
@Table(name="notice_read")
public class NoticeRead implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 公告的阅读记录表需要有：谁阅读的  多对一 【需要外键管理关系】对应User表
	 * 阅读时间  
	 * 阅读那个公告  一对多  对应公告表【Notice】   需要外键管理关系
	 * 
	 * */

	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	
	//阅读公告的用户是谁   一个公告可被多个用户阅读
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	//阅读公告的时间
	@Temporal(TemporalType.TIMESTAMP)//要加上时间戳
	private Date readTime;
	
	//阅读的是哪篇公告？   与公告【Notice关联起来】  关系由这边【多方】管理  
	@ManyToOne()
	@JoinColumn(name="notice_id")	
	private Notice notice;

	/**
	 * 增加构造器，解决之前，在公告首页  自己写查询语句的时候，需要按数组索引位置设置的问题
	 * */
	
	public NoticeRead() {
		
	}
	// 如果没有id、没有user、没有readTime，表示这个公告用户还未阅读
	public NoticeRead(String id, Date readTime, Notice notice) {
		super();
		this.id = id;
		this.readTime = readTime;
		this.notice = notice;
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

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	
}
