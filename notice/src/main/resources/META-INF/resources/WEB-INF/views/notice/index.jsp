<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 引入格式化时间标签 -->

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>公告管理</title>
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
					<c:forEach items="${page.content }" var="n">
						<tr>	
							<td>${n[0].title}</td>
							<td>${n[0].author.name }</td>
							<td><fmt:formatDate value="${n[0].wirteTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td><!-- 格式化时间 调用标签 -->
							<td>
								<!--   判断  三种状态 -->
								<c:choose>
									<c:when test="${n[0].status eq 'DRAFT' }">草稿</c:when>
									<c:when test="${n[0].status eq 'RECALL' }">撤回</c:when>
									<c:when test="${n[0].status eq 'RELEASED' }">发布</c:when>									
								</c:choose>
							</td>
							<td></td>
							
							
						</tr>
					</c:forEach>
				</tbody>				
			</table>
		</div>
	</div>
</div>
</body>
</html>