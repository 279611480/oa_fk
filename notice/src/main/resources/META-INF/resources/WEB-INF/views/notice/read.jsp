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
<title>阅读公告</title>

</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			阅读公告
		</div>
		<div class="panel-body">
			<h1 class="text-center">${notice.title }</h1>
			作者:${notice.author.name } 发布时间：<fmt:formatDate value="${notice.releaseTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
			<hr/>
			${notice.content }
		</div>
		<div class="panel-footer">
			<%-- 如果同时还查询了阅读记录，那么可以在已读之后不要显示【已阅】按钮，而是显示在什么时候已读。 --%>
			<a class="btn btn-primary" onclick="readNotice('${notice.id}')">已阅</a>
		</div>

	</div>
</div>
<script type="text/javascript" src="${ctx }/webjars/wangEditor/3.1.1/release/wangEditor.min.js"></script>
<script type="text/javascript">
    var readNotice = function(id){
    	$.ajax({
    		url: "${ctx}/notice/" + id,
    		method: "post",
    		dataType: "JSON",
    		success: function(){
    			document.location.href="${ctx}/notice";
    		}
    	});
    };
</script>
</body>
</html> 