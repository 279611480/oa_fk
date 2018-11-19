package org.yun.notice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.yun.identity.domain.User;


@Entity
@Table(name="notice")
public class Notice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 公告的表格需要有
	 * 公告id 
	 *  公告标题 
	 *   公告的类型（如：团建互动、人事招聘等   多对一【因为一条公告可能有多种公告类型】 设置主键）
	 *公告的作者  多对一
	 * 公告的撰写时间 （也是最后的一次修改时间） 时间戳  
	 * 发布时间    时间戳
	 * 状态  枚举注解  下面写一个枚举静态类
	 * 公告内容 使用@LOB注解  怕数据库设置大小不够
	 * 一对多  一条公告有多条阅读记录 List集合接收  对应公告阅读记录的notice公告属性
	 * */
	
	
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	
	//公告标题
	private String title;
	
	//公告类型  公告可有多种类型（如：人事招聘，团队活动，文化交流等）
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	private NoticeType type;
	
	//公告作者  多条公告可以是同一个作者发布的
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="author_user_id")
	private User author;
	
	//公告的撰写时间   
	@Temporal(TemporalType.TIMESTAMP)//时间戳
	private Date writeTime;
	
	//公告的发布时间，如果没有发布、还是草稿那么就没有发布时间 
	@Temporal(TemporalType.TIMESTAMP)
	private Date releaseTime;
	
	//公告的状态（草稿DRAFT、发布RELEASED、撤回RECALL）
	@Enumerated(EnumType.STRING)
	private Status status;//调用下面的枚举
	
	//公告内容
	@Lob//大的数据、长的字段，这种字段不适合LIKE（模糊）查询，如果要模糊查询就需要结合后面的Lucene、Solr技术
	private String content;
	
	//为了记录用户的阅读状态，每个用户都会有阅读状态
	//公告的阅读记录  关联公告阅读记录表的字段  关系交给多的一方处理【其实就是外键】
	@OneToMany(mappedBy="notice")
	private List<NoticeRead>reads;
	
	/**枚举公告的状态  被上面调用*/
	public static enum Status{
		/**
		 *草稿 
		 * */
		DRAFT,
		/**
		 * 已发布
		 * */
		RELEASED,
		/**
		 * 已经取消，已撤回
		 * */
		RECALL;	
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public NoticeType getType() {
		return type;
	}


	public void setType(NoticeType type) {
		this.type = type;
	}


	public User getAuthor() {
		return author;
	}


	public void setAuthor(User author) {
		this.author = author;
	}





	public Date getWriteTime() {
		return writeTime;
	}


	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}


	public Date getReleaseTime() {
		return releaseTime;
	}


	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public List<NoticeRead> getReads() {
		return reads;
	}


	public void setReads(List<NoticeRead> reads) {
		this.reads = reads;
	}
	
	
	
	
	
	
	
}
