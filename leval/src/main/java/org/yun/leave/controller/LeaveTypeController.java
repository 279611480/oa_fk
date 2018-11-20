package org.yun.leave.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/leave/type")
public class LeaveTypeController {
	
	@GetMapping//处理URL   跳转至页面
	public ModelAndView index() {
		
		ModelAndView mav = new ModelAndView("leave/type/index");
		return mav;
	}
	
	
	
}
