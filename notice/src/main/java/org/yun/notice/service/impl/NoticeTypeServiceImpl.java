package org.yun.notice.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yun.identity.UserHolder;
import org.yun.identity.domain.User;
import org.yun.notice.domain.Notice;
import org.yun.notice.domain.Notice.Status;
import org.yun.notice.domain.NoticeRead;
import org.yun.notice.domain.NoticeType;
import org.yun.notice.repository.NoticeDao;
import org.yun.notice.repository.NoticeReadDao;
import org.yun.notice.repository.NoticeTypeDao;
import org.yun.notice.service.NoticeTypeService;


@Service
public class NoticeTypeServiceImpl implements NoticeTypeService {

	
	@Autowired
	private NoticeTypeDao noticeTypeDao;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private NoticeReadDao noticeReadDao;
	
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
	
	@Override
	public void write(Notice notice) {
		//01填充字段    表示  全都是  公告一开始的状态
		notice.setAuthor(UserHolder.get());//拿到当前用户【公告作者】
		notice.setWriteTime(new Date());//公告撰写时间
		notice.setReleaseTime(null);//公告发布的时间
		notice.setStatus(Status.DRAFT);//公告一开始就是草稿状态
		if(StringUtils.isEmpty(notice.getId())) {
			notice.setId(null);
		}
		//02.公告id不为空的话 ，那么就调用公告持久层方法保存公告
		this.noticeDao.save(notice);	
		
	}

	@Override
	public Page<NoticeRead> findNotices(Integer number, String keyword) {
		/**
		 * 标题、撰写时间、作者都可以非常方便的查询出来，内容在列表显示的时候，不关心
		 * 状态：草稿、发布、撤回
		 * 还有一个特殊状态：不同的用户有阅读状态，  没有阅读且已经有发布的，使用粗体字显示
		 * 
		 * 要查询的列表数据就包括：
		 * 1.当前用户写的，还未发布的
		 * 2.已经发布、可以阅读，需要查询阅读状态，表关联查询
		 * 3.已经撤回的，只有作则能够查看
		 * */
		
		
		//.公告页面  一般有 作者  以及页面一页可显示多少条数据
		//01通过之前设置的UserHolder拿到当前用户【即作者】
		User author = UserHolder.get();	
		
		//02.设置一页可显示多少条数据
		Pageable pageable = PageRequest.of(number, 10);//第几页，显示多少条数据   其实就是 一页显示多少条数据
		
		//03.设置，公告页面的情况                如果没有阅读状态，那么也会有公告记录
		//Page<Notice> page =this.noticeDao.findNotices(author,pageable);
		Page<NoticeRead> dataPage = this.noticeReadDao.findNotices(author, author, pageable);
		//集合接收，公告页面的内容          
		List<NoticeRead> content = dataPage.getContent();
		//处理关联查询                                        dataPage.getTotalElements()拿到页面的内容数据
		Page<NoticeRead> page = new PageImpl<>(content,pageable,dataPage.getTotalElements());
		return page;
	}


	
	
	
	
	
	
}
