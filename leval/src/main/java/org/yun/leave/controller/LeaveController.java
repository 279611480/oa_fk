package org.yun.leave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/leave")
public class LeaveController {
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("leave/index");
		return mav;
	}	
	
	
}
