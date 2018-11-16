package org.yun.storage.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.hibernate.type.MetaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CharsetEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
	//使用日志工产拿到日志
	private static final Logger LOG =LoggerFactory.getLogger(FileController.class);
	
	
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
		this.wangEditorUpload(file);
		return "redirect:/storage/file";
	}
	
	@PostMapping("wangEditor")
	@ResponseBody
	public WangEditorResponse wangEditorUpload(@RequestParam("file") MultipartFile file) throws IOException {
		// 上传的代码、逻辑还是跟之前一样
		FileInfo info = new FileInfo();
		info.setContentType(file.getContentType());
		info.setFileSize(file.getSize());
 		info.setName(file.getOriginalFilename());
 
 		try (InputStream in = file.getInputStream()) {
			this.storageService.save(info, in);
		}
		//只是返回了一些内容
		WangEditorResponse wangEditorResponse = new WangEditorResponse();
		wangEditorResponse.setErrno(0);//成功
		wangEditorResponse.getData().add("/storage/file/" + info.getId());//图片下载路径
 		return wangEditorResponse; 
		
	}
	//因为要返回JSON格式，不想去找插件  那就自己写
	public static class WangEditorResponse{
		private int errno;
		
		private List<String> data = new LinkedList<>();
		
		public int getErrno() {
			return errno;
		}
		public void setErrno(int errno) {
			this.errno = errno;
		}
		public List<String> getData() {
			return data;
		}
		public void setData(List<String> data) {
			this.data = data;
		}
		
	}
	
	
	
	
	@GetMapping("{id}")
	public ResponseEntity<StreamingResponseBody> download(
			//拿到URL上的id【专属文件所对应的，以免混淆】
			// 文件的id
			@PathVariable("id") String id, //
			//拿到请求头
			@RequestHeader("User-Agent")String userAgent
			)throws IOException {
			//调用持久层方法，根据id查询文件的信息
			FileInfo fi = this.fileService.finById(id);
			//判断，如果说文件的信息为null，那么表明文件信息没找到，返回404
			if(fi == null) {
				LOG.trace("文件信息没找到");
				return send404();//调用下面，自定义的返回404方法
			}
			//不然，说明找到了文件信息	，那么找到，文件内容
			InputStream in = this.fileService.getFileContent(fi);//读取文件内容
			//判断，如果流为空，说明文件内容为空   返回404
			if(in == null) {
				LOG.trace("文件内容没找到");
				return send404();
			}
			/*不然说明根据文件信息，找到了所对应的文件，
			 * 那么开始构建响应体，根据响应体，设置文件大小。（根据文件信息，获取大小）
			*使用MediaType.valueOf()将获取，的文件内容格式，设置为对应的文档类型
			*/
			//构建响应体
			BodyBuilder builder = ResponseEntity.ok();
			//构建文件大小
			builder.contentLength(fi.getFileSize());
			//将下载的文件格式，转换为相对应的文件格式
			builder.contentType(MediaType.valueOf(fi.getContentType()));
			//获取文件名
			String name = fi.getName();
			//对文件名进行编码，避免出现，文件名乱码问题
			name = URLEncoder.encode(name, Charset.forName("utf-8"));
			//告诉浏览器：文件的实际名字
			//Content-Disposition用于告诉浏览器如何处理请求
			//attachment表示响应是一个附件，浏览器要保存起来，于是浏览器，就会弹出文件保存窗口
			// filename告诉浏览器，文件名叫做什么！但是响应头不能有中文，而name是编码后的，没有中文的
			// *=UTF-8'' 注意：结尾是两个单引号，用于告诉浏览器文件名的编码方式
			// 【filename=文件名】这种方式可以用，但是不能兼容所有浏览器
			// 【filename*=UTF-8''】除了IE 7以下的浏览器，其他的全部兼容
			// 一般这里建议：要根据不同的浏览器判断如何返回filename
			// 获取请求头的User-Agent的值来进行判断
			builder.header("Content-Disposition","attachment;filename*=UTF-8''" + name);			
			
			StreamingResponseBody body = new StreamingResponseBody() {
				
				@Override
				public void writeTo(OutputStream out) throws IOException {
					// 当把StreamingResponseBody作为响应体的时候，Spring MVC就会开启异步Servlet的请求
					// 此时相当于在这里面写代码，是在另外一个线程执行的
					try (in) {
						byte[] b = new byte[2048];
						for (int len = in.read(b); //
								len != -1; //
								len = in.read(b)//
						) {
							out.write(b, 0, len);
						}
					}
				}
			};
			ResponseEntity<StreamingResponseBody> entity = builder.body(body);
			return entity;
	}  
	
	
	//发送404的方法
	private ResponseEntity<StreamingResponseBody> send404(){//ResponseEntity响应实体
		BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.NOT_FOUND);//404    构建响应实体，返回状态
		//将相应实体（即内容）转换为相对应的格式，转换为对应的格式
		bodyBuilder.contentType(MediaType.valueOf("text/html;charset=UTF-8"));
		//转换为流   
		StreamingResponseBody body = (out)->{
			out.write("文件没有找到：".getBytes(Charset.forName("UTF-8")));
		};
		return bodyBuilder.body(body);			
	}
	
	
	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathVariable("id")String id) {
		return this.fileService.deleteFile(id);
	}
	
}
