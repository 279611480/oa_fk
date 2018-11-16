package org.yun.notice.service;

import java.util.List;

import org.yun.notice.domain.NoticeType;

public interface NoticeTypeService {

	List<NoticeType> findAllTypes();

	void save(NoticeType type);

	void deleteTypeById(String id);
	
	
	
	
}
