package org.yun.identity.controller;


import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.yun.identity.domain.Role;
import org.yun.identity.domain.User;
import org.yun.identity.service.IdentityService;
import org.yun.identity.service.RoleService;

@Controller
@RequestMapping("/identity/user")
@SessionAttributes({"modifyUserId"})//将传进来的用户id放入线程里面，保证安全性
public class UserController {
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RoleService roleService;
	/***
	 * Model通常是放在方法参数列表中的，用于控制器和JSP传值，也可以作为方法返回值
	 * View只是用来返回页面，可以做返回值
	 * ModelAndView是方法返回值，包含了数据和视图
	 */
	@GetMapping//（接收Url上的请求  跳到index.jsp页面）
	public ModelAndView index(
			@RequestParam(name="pageNumber",defaultValue="0" ) Integer number,//页码从0开始
			@RequestParam(name="keyword",required=false) String keyword//  false  表示 并不是一定要填写
			) {
		ModelAndView mav = new ModelAndView("identity/user/index");
		
		//查询一页的数据
		Page<User> page = identityService.findUsers(keyword,number);
		mav.addObject("page",page);
		return mav;
		
	}
	
	@GetMapping(produces="application/json")
	@ResponseBody
	public AutoCompleteResponse likeName(//调用下面自己定义的类【响应类】   AutoCompleteResponse
			@RequestParam(name="query")String keyword //搜索的关键字     接收页面上传过来的关键字
			) {
		List<User> users =identityService.findUsers(keyword);
		List<User> result = new LinkedList<>();
		users.forEach(user->{
			User u = new User();
			u.setId(user.getId());
			u.setName(user.getName());
			result.add(u);			
		});
		return new AutoCompleteResponse(result);
	}
	/**
	 * 自动完成的响应对象  被上面调用
	 * 里面有响应的参数List集合
	 * 遍历循环
	 * 创建一个响应参数的对象，将其添加到响应参数里面去
	 * 提供get方法拿到响应参数的集合
	 * */
	public static class AutoCompleteResponse{
		//创建响应参数的集合
		private List<AutoCompleteItem> suggestions; //调用下面自己定义的AutoCompleteItem
		//创建构造器，以便被上面里面   创建对象  【自动响应参数的对象】
		public AutoCompleteResponse(List<User> users) {//此处要做的是响应数据库 的用户出去   因为是选择部门经理
			super();
			this.suggestions = new LinkedList<>();
			users.forEach(u -> {
				AutoCompleteItem item = new AutoCompleteItem(u);
				this.suggestions.add(item);
			});
		}
		public List<AutoCompleteItem> getSuggestions() {
			return suggestions;
		}

	}

	/**
	 * 创建AutoComplete被上面调用的been对象
	 * */
	public static class AutoCompleteItem {
		private User user;
		private String value;
		public AutoCompleteItem(User user) {
			super();
			this.user = user;
			this.value = user.getName();
		}
		public User getUser() {
			return user;
		}
		public String getValue() {
			return value;
		}
	}
	
	
	
	@GetMapping("/add")//接收页面上的操作 （处理）  添加
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("identity/user/add");
		//后面这里是需要查询数据，因为每个用户都需要有【身份】、【角色】，在修改用户信息的时候，需要选择用户的身份
		List<Role> roles = this.roleService.findAllNoFixed();
		mav.addObject("roles", roles);
		return mav;
	}
	
	
	
	@PostMapping
	public String save(User user,
			//从Session里面拿到要修改的用户的id
			@SessionAttribute(value="modifyUserId",required=false) String modifyUserId,
			SessionStatus sessionStatus
			) {
			//判断，如果线程id不为空，且用户id在数据库有的情况下，
			//将线程里面获取的id设置给用户，以保证要修改的是同一个用户
			if(modifyUserId != null && !StringUtils.isEmpty(user.getId())) {
				user.setId(modifyUserId);
			}
			
			identityService.save(user);
		
			//清理现场
			sessionStatus.setComplete();
			
			return "redirect:/identity/user";
		}
	
	
	
	//@PathParam("id") String id  从URL中获取id
	@GetMapping("/{id}")
	public ModelAndView detail(@PathVariable("id") String id) {
		//页面跟添加一样（不用再去弄新页面）,只需要(用到添加页面即可)把User根据id查询出来即可
		ModelAndView mav = this.add();
		
		User user = this.identityService.findUserById(id);
		
		//将要修改的user添加到ModelAndView（线程）里面即可
		mav.addObject("user",user);
		//将要修改的user（用户）的id，存储在session里面，避免在浏览器恶意修改
		mav.addObject("modifyUserId",user.getId() );
		return mav;
	}
	
	@GetMapping("/active/{id}")
	public String active(@PathVariable("id") String id) {
		this.identityService.active(id);
		return "redirect:/identity/user";
	}
	
	@GetMapping("/disable/{id}")
	public String disable(@PathVariable("id") String id) {
		this.identityService.disable(id);
		return "redirect:/identity/user";
	}
	
	
}
