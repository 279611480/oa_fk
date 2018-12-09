package org.yun.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yun.workflow.service.WorkflowService;
import org.yun.workflow.vo.ProcessForm;

@Controller
@RequestMapping("/workflow/instance")
public class InstanceController {
	@Autowired
	private WorkflowService workflowService;
	
	@GetMapping("{key}")
	public ModelAndView start(@PathVariable("key") String key) {
		ModelAndView mav = new ModelAndView("workflow/instance/start");
		ProcessForm form = this.workflowService.findDefinitionByKey(key);
		mav.addObject("form",form);
		return mav;
	}
	
}
