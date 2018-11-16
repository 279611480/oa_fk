package org.yun.notice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.yun.notice.domain.NoticeType;
import org.yun.notice.repository.NoticeTypeDao;
import org.yun.notice.service.NoticeTypeService;

@Service
public class NoticeTypeServiceImpl implements NoticeTypeService {

	
	@Autowired
	private NoticeTypeDao noticeTypeDao;
	
	@Override
	public List<NoticeType> findAllTypes() {
		Sort sort = Sort.by("name");//按名字排序
		return this.noticeTypeDao.findAll(sort);
	}

	@Override
	public void save(NoticeType type)  {
		// 根据类型名字，查询出所有公告
		NoticeType old = this.noticeTypeDao.findByName(type.getName());
		
		//判断，如果是新增的/修改的公告  调用持久层方法保存数据
		if(old == null || old.getId().equals(type.getId())) {
			this.noticeTypeDao.save(type);
		}else {//不然判处异常公告名重复
			throw new IllegalArgumentException("公告类型的名称不能重复！");
		}
	}

	@Override
	public void deleteTypeById(String id) {
		// 调用持久层方法根据数据id删除数据
		 this.noticeTypeDao.deleteById(id);		
	}
	
	
	
	
	
	
}
