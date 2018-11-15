package org.yun.storage.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yun.identity.domain.User;
import org.yun.storage.domain.FileInfo;
@Repository
public interface FileDao extends JpaRepository<FileInfo, String> {
	
	Page<FileInfo> findByOwner(User user, Pageable pageable);
	Page<FileInfo> findByOwnerAndNameContaining(User user, String keyword, Pageable pageable);

}
