package org.yun.notice.controller;

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
import org.yun.notice.domain.NoticeType;
import org.yun.notice.service.NoticeTypeService;

@Controller
@RequestMapping("/notice/type")//接收页面传输过来的URL
public class NoticeTypeController {
	
	@Autowired
	private NoticeTypeService noticeService;
	
	@GetMapping//处理URL   跳转至页面
	public ModelAndView index() {
		
		ModelAndView mav = new ModelAndView("notice/type/index");
		
		//调用服务层方法，查询，数据库所有的公告类型   ，且用list集合接收
		List<NoticeType> types = noticeService.findAllTypes();
		//添加到视图里面
		mav.addObject("types",types);
		return mav;
	}
	
	
	@PostMapping
	public String save(NoticeType type) {
		 noticeService.save(type);
	
		 return "redirect:/notice/type";
	}
	
	//根据数据id删除数据
	@DeleteMapping
	@ResponseBody
	public String delete(@PathVariable("id")String id ) {
		this.noticeService.deleteTypeById(id);	
		return "OK";
	}
	
}
