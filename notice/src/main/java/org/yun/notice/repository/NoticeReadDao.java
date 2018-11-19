package org.yun.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yun.identity.domain.User;
import org.yun.notice.domain.Notice;
import org.yun.notice.domain.NoticeRead;

@Repository
public interface NoticeReadDao extends JpaRepository<NoticeRead, String> {
//	case n.status 
//	when 'DRAFT' 
//		then 0
//	when 'RECALL'
//		then 99 
//	when 'RELEASED' 
//		then 1 
//end
	
	/**
	 * 自己写查询语句
	 *【Switch...case】 
	 *设置条件 相当于 子查询  当公告状态为草稿时 那么将其设置为0  为撤回状态时设置为99  为发布状态时  设置为1   
	 * where  条件  当公告状态是当前作者 而且  状态为草稿或者撤回或者发布的时候   
	 * 根据状态【上面设置的数字升序排序（asc即  从上往下是  1-5），desc发布时间倒序排序（即  从上往下是5-1）】 
	 * */
	// select * from Notice n left outer join NoticeRead nr on nr.notice = n and nr.user = :user
		// where  (n.author = :author and (n.status = 'DRAFT' or n.status = 'RECALL')...	
	@Query("select new NoticeRead(nr.id, nr.readTime, n)"//
			+ " from Notice n "//
			+ " left outer join NoticeRead nr on nr.notice = n and nr.user = :user "
			+ " where (n.author = :author and (n.status = 'DRAFT' or n.status = 'RECALL') "//
			+ " or n.status = 'RELEASED')" //
			+ " order by case n.status when 'DRAFT' then 0 when 'RECALL' then 99 when 'RELEASED' then 1 end asc, n.releaseTime desc")
	Page<NoticeRead> findNotices(@Param("author") User author, @Param("user") User user, Pageable pageable);

	NoticeRead findByNoticeAndUser(Notice notice, User user);



	
	
	
}
