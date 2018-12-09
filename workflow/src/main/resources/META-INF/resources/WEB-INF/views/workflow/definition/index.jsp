<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 引用JSP Tag文件 --%>
<%@ taglib prefix="yun" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>流程定义管理</title>
</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			流程定义
			<div class="close">
				<a class="btn btn-default"
					data-toggle="modal" data-target="#uploadDefinitionFile">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<!-- Table -->
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<th>分类</th>
						<th>名称</th>
						<th>KEY</th>
						<th>版本号</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.content }" var="u">
					<tr>
						<td>${u.category }</td>
						<td>${u.name }</td>
						<td>${u.key }</td>
						<td>${u.version }</td>
						<td>
							<c:choose>
								<%-- 枚举在EL表达式里面，直接当做字符串来使用 --%>
								<c:when test="${u.suspended }">
									<a href="${ctx }/workflow/definition/active/${u.id}">激活</a>
								</c:when>
								<c:otherwise>
									<a href="${ctx }/workflow/definition/disable/${u.id}">禁用</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align: center;">
							<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
							<yun:page url="/workflow/definition?keyword=${param.keyword }" page="${page }"/>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>

<!-- 增加上传文件的布局  功能 -->
<!-- Button trigger modal -->

<!-- Modal -->
<!-- Modal -->
<div class="modal fade" id="uploadDefinitionFile" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<form action="" method="post" enctype="multipart/form-data">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">上传流程定义压缩文件</h4>
				</div>
				<div class="modal-body">
					请选择使用ZIP格式压缩流程定义所有的相关文件，包括bpmn、png、表单。建议直接在Eclipse中导出zip格式的压缩文件。<br/>
					<input name="file" type="file" required="required"/>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="submit" class="btn btn-primary">上传</button>
				</div>
			</div>
			<input type="hidden"
				name="${_csrf.parameterName}"
				value="${_csrf.token}"/>
		</form>
	</div>
</div>
</body>
</html>