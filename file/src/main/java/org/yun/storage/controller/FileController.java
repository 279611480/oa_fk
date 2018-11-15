package org.yun.storage.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.yun.common.data.domain.Result;
import org.yun.storage.domain.FileInfo;
import org.yun.storage.service.StorageService;


@Controller
@RequestMapping("/storage/file")
public class FileController {
	
	@Autowired
	private StorageService storageService;
	
	
	@GetMapping
	public ModelAndView index(//
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer number, //
			@RequestParam(name = "keyword", required = false) String keyword) {
		
		ModelAndView mav = new ModelAndView("storage/file/index");
		Page<FileInfo> page =this.storageService.findFiles(keyword,number);
		mav.addObject("page",page);
		return mav;
	}
	
	
	
	@Autowired
	private StorageService fileService;
	
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
		FileInfo info = new FileInfo();
		info.setContentType(file.getContentType());
		info.setFileSize(file.getSize());
		info.setName(file.getOriginalFilename());	
		
		try(InputStream in = file.getInputStream()) {
			this.storageService.save(info,in);
		} 
		
		return "redirect:/storage/file";
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<StreamingResponseBody> download(@PathParam("id")String id){
		return null;
	}  
	
	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathParam("id")String id) {
		return null;
	}
	
}
