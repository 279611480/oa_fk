package org.yun.menu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yun.menu.domain.Menu;

@Repository
public interface MenuDao extends JpaRepository<Menu,String> {

	Menu findByNameAndParent(String name, Menu parent);

	Menu findByNameAndParentNull(String name);
	
	@Query("select max(number) from Menu where parent is null")
	Double findMaxNumberByParentNull();

	// 使用冒号是命名参数，通过参数名来传值
	// 使用使用?1表示非命名参数，通过第几个参数类传值
	@Query("select max(number) from Menu where parent = :parent")
	Double findMaxNumberByParent(@Param("parent") Menu parent);



	List<Menu> findByParentNullOrderByNumber();

	Page<Menu> findByParentAndNumberLessThanOrderByNumberDesc(Menu parent, Double number, Pageable pageable);

	Page<Menu> findByParentAndNumberGreaterThanOrderByNumberAsc(Menu parent, Double number, Pageable pageable);

	
	
	
	
	
}
