	package org.yun.storage.service.impl;



	import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yun.common.data.domain.Result;
import org.yun.identity.UserHolder;
import org.yun.identity.domain.User;
import org.yun.storage.dao.FileDao;
import org.yun.storage.domain.FileInfo;
import org.yun.storage.service.StorageService;


@Service
@ConfigurationProperties(prefix="yun.storage")//对应yaml的35   见47行
public class StorageServiceImpl implements StorageService {
	
	private static final Logger LOG= LoggerFactory.getLogger(StorageServiceImpl.class);//日志记录
	
	//使用普通Spring项目的时候，通过PropertyPlaceHolder方法加载文件的时候，可以利用@Vlaue注入属性的值
	//冒号后面不能有空格，最好前面也不要有
	//冒号后面表示【默认值】
	//@Value(value="yun.storage.dir?:E:/JAVATem/XNM")
	
	//在Spring Boot，配置@ConfigurationProperties注解和set方法，即可得到application.yaml文件中的参数
	//等号后面初始值是在没有配置参数的时候使用的默认值
	private String dir ="E:/JAVATem/XNM" ;



	public void setDir(String dir) {
		this.dir = dir;
	}



	@Autowired
	private FileDao fileDao;

	@Override
	public void save(FileInfo info, InputStream in) {
		//保存文件，需要找到文件的路径   使用UUID随机生成一个路径
		String path = UUID.randomUUID().toString();
		//创建一个文件 将保存的文件目录  以及路径传进去
		File file = new File(dir,path);
		//判断，如果上面，保存文件的目录里面的文件   拿到它的上级目录是空的，那么创建一个
		if(!file.getParentFile().exists() ) {
			file.getParentFile().mkdirs();
		}
		
		LOG.trace("文件实际存储的目录：" + file.getAbsolutePath());
		
		//否则，拿到目标文件的路径
		Path target =file.toPath();
		//try...catch一下  文件调用copy方法，将之前在控制器拿到的输入流里面的信息，以及目标文件路径传进去
		//不然就是抛出异常，打印异常详细信息
		try {
			Files.copy(in,target);
				
		} catch (Exception e) {
			throw new RuntimeException("文件保存硬盘失败：" + e.getLocalizedMessage(),e);
		}
		
		//保存文件信息
		info.setOwner(UserHolder.get());
		info.setUploadTime(new Date());
		info.setPath(path);
		//调用持久层方法保存  将文件传入进去
		FileInfo fi  = this.fileDao.save(info);
		//为了把id返回给控制器使用
		info.setId(fi.getId());
	}



	@Override
	public Page<FileInfo> findFiles(String keyword, Integer number) {
		//只查询当前用户的文件
		//01.要拿到当前用户   可调用之前的UserHolder的get方法  拿到当前用户
		User user = UserHolder.get();
		//关键字搜索【判断有无关键字，有的话直接将关键字设置为null】
		if(StringUtils.isEmpty(keyword)) {
			keyword = null;
		}
		//设置分页标签
		//设置每页显示10条数据
		Pageable pageable = PageRequest.of(number, 10);
		//创建页面
		Page<FileInfo> page;
		//判断有无关键字
		if(keyword == null ) {//如果有（说明没有使用关键字搜索），调用持久层方法，查询当前用户的数据
			page = this.fileDao.findByOwner(user,pageable);
		}else {//如果没有关键字，将关键字传入即可   【Containing模糊查询】
			page = this.fileDao.findByOwnerAndNameContaining(user,keyword,pageable);	
		}
		//返回页面
		return page;
	}



	@Override
	public FileInfo finById(String id) {
		return this.fileDao.findById(id).orElse(null);
	}



	@Override
	public InputStream getFileContent(FileInfo fi) throws FileNotFoundException {
		try {
			File file = new File(dir,fi.getPath());
			FileInputStream inputStream = new FileInputStream(file);
			return inputStream;
		} catch (FileNotFoundException e) {
			LOG.trace("文件没有找到：" + e.getLocalizedMessage(), e);
			return null;
		}
		
	}



	@Override
	public Result deleteFile(String id) {
		//1.根据id获取文件信息   没找到就返回null
		FileInfo info = this.fileDao.findById(id).orElse(null);
		if(info != null) {
		//2.说明找到了文件
		//那么删除硬盘上的文件夹
		//先找到文件夹路径，再删除
		File file = new File(dir,info.getPath());	
			file.delete();
		//删除，文件信息	
		this.fileDao.delete(info);	
		}
	return null;
	}

}

