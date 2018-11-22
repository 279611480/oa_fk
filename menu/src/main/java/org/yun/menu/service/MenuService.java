package org.yun.menu.service;

import java.util.List;
import java.util.Set;

import org.yun.common.data.domain.Result;
import org.yun.menu.domain.Menu;

public interface MenuService {

	void save(Menu menu);

	List<Menu> findTopMenus();

	Result move(String id, String targetId, String moveType);

	Result delete(String id);

	
	Set<String> findMyUrls(String userId);

	List<Menu> findMyMenus(String userId);


	
}
