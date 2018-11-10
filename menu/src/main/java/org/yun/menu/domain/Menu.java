package org.yun.menu.domain;

import java.io.Serializable;
import java.util.List;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.yun.identity.domain.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(length = 36)
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String id;
	private String name;
	private String url;
	@Enumerated(EnumType.STRING)
	private Type type;
	@ManyToMany
	@JoinTable(name = "menu_roles")
	@OrderBy("name") // 使用角色的名称排序
	private List<Role> roles;
	// 排序的序号
	private Double number;
	// 上级菜单、父菜单
	@ManyToOne()
	@JoinColumn(name = "parent_id") // 上级菜单的id\
	@JsonIgnore
	private Menu parent;
	// 下级菜单
	@OneToMany(mappedBy = "parent")
	@OrderBy("number") // 查询下级菜单的时候，使用number进行排序
	@JsonProperty("children") // 生成JSON的时候，使用一个别名
	//防止无限递归  因为数据库的属性名  与 页面传进去的数据的名字不是childs而是children
	private List<Menu> childs;

	public static enum Type {
		/**
		 * 链接类型的
		 */
		LINK,
		/**
		 * 按钮
		 */
		BUTTON;
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChilds() {
		return childs;
	}

	public void setChilds(List<Menu> childs) {
		this.childs = childs;
	}
}
