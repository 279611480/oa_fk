package org.yun.file.service;

import java.io.InputStream;

import org.yun.identity.domain.User;



public interface FileService {

	void save( String name, String contentType, long fileSize, InputStream in);

	
	
	
	
	
	
}
