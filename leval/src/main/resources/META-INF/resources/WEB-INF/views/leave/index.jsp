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
<title>请假管理</title>


</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			请假管理
			<div class="close">
				<a class="btn btn-default" href="${ctx }/leave/add">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<td>请假人</td>
						<td>请假时间</td>
						<td>请假类型</td>
						<td>请假工资扣除比例</td>
						<td>请假状态</td>
						<td>操作</td>						
					</tr>
				</thead>
				<!--将页面中的内容  遍历出来 对应上面头部  -->
				<tbody>
				将请假的内容循环遍历出来好了
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align: center;">
<%-- 							<yun:page url="/leave?keyword=${param.keyword }" page="${page }"/>
 --%>						</td>
					</tr>
				</tfoot>
								
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
	
</script>


</body>	
</html>