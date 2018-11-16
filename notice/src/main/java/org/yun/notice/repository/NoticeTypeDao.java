package org.yun.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yun.notice.domain.NoticeType;

@Repository
public interface NoticeTypeDao extends JpaRepository<NoticeType, String> {

	NoticeType findByName(String name);

	
	
	
	
	
}
