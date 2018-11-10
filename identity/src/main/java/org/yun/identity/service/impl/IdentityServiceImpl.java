package org.yun.identity.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.yun.identity.domain.Role;
import org.yun.identity.domain.User;
import org.yun.identity.domain.User.Status;
import org.yun.identity.repository.RoleDao;
import org.yun.identity.repository.UserDao;
import org.yun.identity.service.IdentityService;

@Service
public class IdentityServiceImpl implements IdentityService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	@Override
	@Transactional
	public void save(User user) {
		//System.out.println(user.getRoles().size());
		//处理角色：固定的始终固定有，角色不能重复
		//1.调用持久层方法根据固定角色是否有查询   用list集合接收数据库的固定角色
		List<Role> fixedRoles = this.roleDao.findByFixedTrue();
		
		//2.页面传过来的角色   
		//调用user提供的user.getRoles()方法接收来自页面传输过来的角色   且定义list集合接收，如果页面传了个空的集合进来 ，那么自己创建一个linkedList集合   且设置进去
		List<Role> roles =user.getRoles();
		
		if(roles == null) {
			roles = new LinkedList<>();
			user.setRoles(roles);
		}else {
			//根据页面传递过来的id，查询所有角色
			List<String> ids = new LinkedList<>();
			roles.stream()//转换为流式API
			.map((role)->{
				//role和id的映射
				//我们希望：把role里面的id全部获取出来
				return role.getId();
			}).forEach(id->{
				//迭代每个id，把id加入集合
				ids.add(id);
			});
			
//			for(Role r : roles) {
//			String id = r.getId();
//			ids.add(id);
//		}

//		roles.stream().map(role -> role.getId()).forEach(ids::add);
			
			List<Role> tmp =this.roleDao.findAllById(ids);
			roles.clear();
			// 把页面传过来的角色，全部从数据库里面查询一遍！
			roles.addAll(tmp);
		}
		//整理角色
 		//3.确保角色不能重复，需要重写Role的equals方法和hashCode方法，把所有角色添加到Set里面，就不会重复了。（因为Set集合是无序不可重复的）
		Set<Role> allRoles =new HashSet<>(); 
		allRoles.addAll(fixedRoles);//接收数据库固有的角色
		allRoles.addAll(roles);//如果后面传进来的角色，已经存在Set里面，不会加入！			接收页面上传过来的角色     
		/*System.out.println();
		System.out.println();
		System.out.println("fixedRoles     "+fixedRoles);
		System.out.println();
		System.out.println("roles          "+roles);
		System.out.println();
		System.out.println("allRoles       "+allRoles);
		System.out.println();
		System.out.println();*/
		//将整理后的角色放入User里面
		roles.clear();
		roles.addAll(allRoles);//roles是User.java定义的字段
		
		
		//1.检查登录名是否有Id，如果没有id则直接把id设置为null，方便新增
		if(StringUtils.isEmpty(user.getId())) {
			user.setId(null);
		}
		
		//每次修改以后，都是用正常状态
		user.setStatus(Status.NORMAL);
		user.setExpiredTime(getExpriedTime());
		
		
		//2.根据登录名查询（数据库中的）User对象，用于判断登录名是否被占用
		User old = userDao.findByLoginName(user.getLoginName());
		if(old == null) {//没对像的话，没被占用，直接保存
			//没被占用，但是有id（id不为空）的情况，可能是修改，并且密码也为空 的话，
			//就需要设置依旧使用旧密码，不然就使用新密码
			if(!StringUtils.isEmpty(user.getId())
					&& StringUtils.isEmpty(user.getPassword())
					) {
				//根据id拿到与数据库相同的用户id
				old = userDao.findById(user.getId()).orElse(null);
				//设置使用原本 密码
				user.setPassword(old.getPassword());	
			}else {
				//使用新密码
				String password = this.passwordEncoder.encode(user.getPassword());
				user.setPassword(password);
			}
			userDao.save(user);
		}else {
			//被占用的话  
			//情况之一：可能修改的时候登录名没有改变   ，那么，引发下面判断
			// 有id表示修改，并且页面传入的id跟数据库查询得到的id相同。
			// 此时表示：页面的登录名和数据库的登录名相同，但是属于同一个用户。所以仍然可以保存
			if (user.getId() != null && user.getId().equals(old.getId())) {
			//有被占用，属于同一用户，但是如果没有密码的话，
			//那么，使用的密码（不作修改的情况,原本是什么，保存完就是什么），
			//不然，页面传进去的密码就是新的密码	
				if(StringUtils.isEmpty(user.getPassword())) {
					//修改情况下,密码为空那么，直接使用,之前设置的密码
					user.setPassword(old.getPassword());
				}else {
					//使用新密码
					String password = this.passwordEncoder.encode(user.getPassword());
					user.setPassword(password);
				}
				this.userDao.save(user);
			}else {
				throw new  IllegalArgumentException("登录名重复//非法参数异常");
			}
			
		}

		
	}

	@Override
	public Page<User> findUsers(String keyword, Integer number) {
		if(StringUtils.isEmpty(keyword)) {
			keyword =null;
		}
		//分页条件
		Pageable pageable = PageRequest.of(number, 4); //使用Spring自己提供的Pageable实现分页
		
		Page<User> page;
		
		if(keyword == null) {
			//分页查询所有数据
			page =userDao.findAll(pageable);
		}else {
			//根据姓名来查询,前后模糊查询
			page = userDao.findByNameContaining(keyword,pageable);
		}
		return page;
		
	}
	
	@Override
	public User findUserById(String id) {
		// orElse ：如果没有从数据库找到对象，那么返回orElse的参数（自己设置的）
		return userDao.findById(id).orElse(null);
	}

	
	private Date getExpriedTime() {
		//加上两个月   获取月份方法
		Calendar cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);//获取月份
		month+=2;
		cal.set(Calendar.MONTH, month);//把修改的月份设置回去
		
		//2个月之后的时间
		Date time = cal.getTime();
		return time;
	}	
	
	
	@Override
	@Transactional
	public void active(String id) {
		User user = this.userDao.findById(id).orElse(null);
		if(user != null) {
			user.setExpiredTime(getExpriedTime());
			user.setStatus(Status.NORMAL);
		}
		
		
		
	}


	@Override
	@Transactional
	public void disable(String id) {
		User user = this.userDao.findById(id).orElse(null);
		if(user != null) {
		
			user.setStatus(Status.DISABLED);
		}
		
	}

}
