package org.yun.file.service.impl;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.yun.file.dao.FileDao;
import org.yun.file.domain.FileInfo;
import org.yun.file.service.FileService;
import org.yun.identity.domain.User;

@Service
public class FileServiceImpl implements FileService, InitializingBean {

	@Autowired
	private FileDao fileDao;
	// 使用编程式事务
	@Autowired
	private PlatformTransactionManager transactionManager;

	// 文件的实际内容保存的目录，文件名是随机的
	private File dir = new File("E:/JAVATem/XNM");

	// 实现InitializingBean接口以后，那么Bean在创建并注入完成之后，会调用afterPropertiesSet
	@Override
	public void afterPropertiesSet() throws Exception {
		// 如果目录不存在，就直接创建一下
		if (!dir.exists()) {
			dir.mkdirs();
		}
		System.out.println("实际文件的存储目录：" + dir.getAbsolutePath());
	}

	@Override
	public void save( String name, String contentType, long fileSize, InputStream in) {
		String fileName = UUID.randomUUID().toString();

		// 保存文件内容
		File file = new File(dir, fileName);
		Path target = file.toPath();

		try {
			Files.copy(in, target);
		} catch (IOException e) {
			// 保存文件出现问题，就不要保存文件信息到数据库
			throw new RuntimeException(e.getMessage(), e);
		}

		FileInfo info = new FileInfo();
		info.setContentType(contentType);
		info.setFileName(fileName);
		info.setFileSize(fileSize);
		info.setName(name);


		// 保存文件信息
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		fileDao.save(info);
		transactionManager.commit(status);
	}

	
}

