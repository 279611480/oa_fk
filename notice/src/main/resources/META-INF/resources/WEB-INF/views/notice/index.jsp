<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 引入格式化时间标签 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 引入自定义分页标签 -->
<%@ taglib prefix="yun" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>公告管理</title>
<!-- 增加，已发布，但是没有被阅读的时候的状态   加粗  实现的样式 -->
<style type="text/css">
.unread{
	font-weight: bold;/* 宽度加粗 */
}
</style>

</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			公告管理
			<div class="close">
				<a class="btn btn-default" href="${ctx }/notice/add">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<td>标题</td>
						<td>作者</td>
						<td>撰写时间</td>
						<td>状态</td>
						<td>操作</td>						
					</tr>
				</thead>
				<!--将页面中的内容  遍历出来 对应上面头部  -->
				<tbody>
					<c:forEach items="${page.content }" var="nr"><!-- 改为nr是因为，公告阅读持久层里面，
					使用注解写查询语句的时候，将公告阅读左外连接公告【以公告阅读为主  其里面的表有公告的字段】 -->
						<tr class="${(nr.notice.status eq 'RELEASED' and empty nr.readTime) ? 'unread': '' }">	
							<td>${nr.notice.title }</td>
							<td>${nr.notice.author.name }</td>
							<td><fmt:formatDate value="${nr.notice.writeTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td><!-- 格式化时间 调用标签 -->
							<td>
								<!--   判断  三种状态 -->
								<c:choose>
									<c:when test="${nr.notice.status eq 'DRAFT' }">草稿</c:when>
									<c:when test="${nr.notice.status eq 'RECALL' }">撤回</c:when>
									<c:when test="${nr.notice.status eq 'RELEASED' }">已发布</c:when>							
								</c:choose>
							</td>
							<td></td>
							
							
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align: center;">
							<yun:page url="/notice?keyword=${param.keyword }" page="${page }"/>
						</td>
					</tr>
				</tfoot>
								
			</table>
		</div>
	</div>
</div>
</body>
</html>