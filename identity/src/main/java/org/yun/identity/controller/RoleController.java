package org.yun.identity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yun.identity.domain.Role;
import org.yun.identity.repository.RoleDao;
import org.yun.identity.service.RoleService;

@Controller
@RequestMapping("/identity/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("/identity/role/index");
		
		List<Role> roles = roleService.findAllRoles();
		
		mav.addObject("roles", roles);
		return mav;
	}
	
	@PostMapping
	public String save(Role role) {
		this.roleService.save(role);
		return "redirect:/identity/role";
	}
	
	@DeleteMapping("{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		this.roleService.deleteById(id);
		return "OK";
		
	}
}
