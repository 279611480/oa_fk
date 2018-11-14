package org.yun.file.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yun.file.domain.FileInfo;
@Repository
public interface FileDao extends JpaRepository<FileInfo, String> {
	
}
