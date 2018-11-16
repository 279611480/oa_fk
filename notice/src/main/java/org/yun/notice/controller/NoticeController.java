package org.yun.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yun.notice.domain.NoticeType;
import org.yun.notice.service.NoticeTypeService;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	private NoticeTypeService noticeTypeService;
	
	
	@GetMapping
	public ModelAndView index(
			@RequestParam(name="pageNumber",defaultValue="0")String number,
			@RequestParam(name = "keyword", required = false) String keyword
			) {
		ModelAndView mav = new ModelAndView("/notice/index");
		return mav;
	}
	
	@GetMapping("add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("/notice/add");
		List<NoticeType> types = noticeTypeService.findAllTypes();
		mav.addObject(types);
		return mav;
	}
	
	
	
	
	
}
