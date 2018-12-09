package org.yun.workflow.controller;

import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yun.workflow.service.WorkflowService;

@Controller
@RequestMapping("/workflow/definition")
public class DefinitionController {
	//注入  流程引擎服务层
	@Autowired
	private WorkflowService workflowService;
	//列表   Get请求  参数有关键字（不是必须）以及页面（默认值为0，即第一页）
	//调用服务层方法，根据关键字，以及页码，查询流程定义
	//创建ModelAndView，跳转至首页   返回mav
	@GetMapping()
	public ModelAndView index(
			@RequestParam(name="keyword",required=false)String keyword,
			@RequestParam(name="pageNumber",defaultValue="0")int pageNumber
			) {
		Page<ProcessDefinition> page = this.workflowService.findDefinitions(keyword, pageNumber);
		ModelAndView mav = new ModelAndView("workflow/definition/index");
		mav.addObject("page",page);
		return mav;
	}
	
	@GetMapping("disable/{id}")
	public String disable(@PathVariable("id") String id) {
		this.workflowService.disableProcessDefinition(id);
		return "redirect:/workflow/definition";
	}
	
	@GetMapping("active/{id}")
	public String active(@PathVariable("id") String id) {
		this.workflowService.activeProcessDefinition(id);
		return "redirect:/workflow/definition";
	}
	
	@PostMapping
	public String upload(@RequestParam("file") MultipartFile file ) throws IOException {
		String name = file.getOriginalFilename();
		try (InputStream in =file.getInputStream()){
			this.workflowService.deploy(name, in);
		} 
		return "redirect:/workflow/definition";
	}
//	@PostMapping
//	public String upload(@RequestParam("file") MultipartFile file) throws IOException {
//		String name = file.getOriginalFilename();
//		try (InputStream in = file.getInputStream()) {
//			this.workflowService.deploy(name, in);
//		}
//		return "redirect:/workflow/definition";
//	}
}
