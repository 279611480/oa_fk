package org.yun.layout.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller//表示这是一个控制层
@RequestMapping("/layout")//使用这个注解，表示，无论页面传过来的是Get还是Post请求都能接收，且处理
public class IndexController {

	
	@RequestMapping//接收到页面URL请求后，这里跳转到index.jsp页面
	public String index() {
		return "layout/index";
	}
	
	
	
}
