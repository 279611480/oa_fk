package org.yun.file.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import org.yun.file.service.FileService;


@Controller
@RequestMapping("/file")

public class FileController {
	
	
	@GetMapping()
	public String index() {
		//yaml配置视图解析器    应该把首页位置放到views里面去，因为视图管理器，是到哪里找文件
		return "redirect:/index.jsp";
	}
	
	
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 
	 * @param file 上传的文件
	 * @param user 当前的登录用户
	 * @return
	 * @throws IOException
	 */
	@PostMapping
	public String upload(@RequestParam("file") MultipartFile file//
			)//
			throws IOException {
		// System.out.println(file.getOriginalFilename());// 文件名
		// System.out.println(file.getSize());// 文件大小
		String name = file.getOriginalFilename();
		String contentType = file.getContentType();// 文件类型
		long fileSize = file.getSize();

		// 流里面就是文件内容
		try (InputStream in = file.getInputStream()) {
			fileService.save( name, contentType, fileSize, in);
		}
		return "redirect:/index.jsp";
	}
	
	
}
