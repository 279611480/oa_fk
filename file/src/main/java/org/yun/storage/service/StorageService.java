package org.yun.storage.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.data.domain.Page;
import org.yun.common.data.domain.Result;
import org.yun.identity.domain.User;
import org.yun.storage.domain.FileInfo;



public interface StorageService {
	
	
	void save(FileInfo info, InputStream in);
	Page<FileInfo> findFiles(String keyword, Integer number);
	
	FileInfo finById(String id);

	InputStream getFileContent(FileInfo fi) throws FileNotFoundException;
	
	Result deleteFile(String id);
	
}
