package org.yun.hr.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yun.hr.domain.Department;
import org.yun.hr.service.HumanResourceService;
import org.yun.identity.domain.User;
import org.yun.identity.service.IdentityService;

@Controller
@RequestMapping("/human-resource/department")
public class DepartmentController {
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private HumanResourceService humanResourceService;
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView mav= new ModelAndView("human-resource/department/index");
		
		//模拟查询，后面要改为Ajax   只是模拟的  查到当前用户而已
		Page<User> userPage = this.identityService.findUsers(null, 0);
		List<User> user = userPage.getContent();//拿到当前部门，所有员工的信息
		mav.addObject("user",user);
		return mav;
	}
	
	//调用服务层方法，查询上级部门  用List集合接收
	//如果客户端，要求返回JSON的时候，调用下面这个方法
	@GetMapping(produces="application/json")
	@ResponseBody
	public List<Department> findTopDepartments(){
		return this.humanResourceService.findTopDepartments();
	}
	
	
	//保存   调用人事服务层方法保存部门
	@PostMapping
	public String save(Department department) {
		this.humanResourceService.save(department);
		return "redirect:/human-resource/department";//重定向到部门首页
	}
	
	
	
}
