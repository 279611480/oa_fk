package org.yun.notice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.yun.notice.domain.Notice;
import org.yun.notice.domain.NoticeRead;
import org.yun.notice.domain.NoticeType;

public interface NoticeTypeService {

	List<NoticeType> findAllTypes();

	void save(NoticeType type);

	void deleteTypeById(String id);

	Page<NoticeRead> findNotices(Integer number, String keyword);//设置公告页面的分页标签   以及 公告页面的关键字搜索

	void write(Notice notice);//将添加【保存】到的公告  写出来  显示在公告页面
	
	
	
	
}
