package org.yun.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yun.notice.domain.Notice;
import org.yun.notice.domain.NoticeType;
import org.yun.notice.service.NoticeTypeService;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	private NoticeTypeService noticeTypeService;
	
	
	@GetMapping
	public ModelAndView index(
			@RequestParam(name="pageNumber",defaultValue="0")Integer number,
			@RequestParam(name = "keyword", required = false) String keyword
			) {
		ModelAndView mav = new ModelAndView("/notice/index");
		//设置公告页面分页标签与关键字搜索到公告首页去  调用公告类型服务层  根据传进去的页码数与关键字
		Page<Notice> page = this.noticeTypeService.findNotices(number,keyword);
		mav.addObject("page", page);
		return mav;
	}
	
	@GetMapping("add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("/notice/add");
		List<NoticeType> types = noticeTypeService.findAllTypes();
		mav.addObject("types",types);
		return mav;
	}
	
	//接收添加公告页面的保存请求
	@PostMapping()
	public ModelAndView save(Notice notice) {
		//保存以后，重定向到公告页面
		ModelAndView mav = new ModelAndView("redirect:/notice");	
		this.noticeTypeService.write(notice);
		return mav;
	}
	
	
	
}
