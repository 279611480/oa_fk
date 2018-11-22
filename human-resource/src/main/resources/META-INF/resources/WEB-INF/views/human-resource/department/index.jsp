<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 引用JSP Tag文件 --%>
<%@ taglib prefix="yun" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>部门管理</title>
<%-- 所有放到static、public、resources里面的文件，都是在根目录的 --%>
<link rel="stylesheet" href="${ctx }/zTree/css/zTreeStyle/zTreeStyle.css"/>
<%-- async="async"表示异步加载js --%>
<script type="text/javascript" src="${ctx }/zTree/js/jquery.ztree.all.min.js"></script>
</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			部门管理

		</div>
		<div class="panel-body">
			<div class="col-xs-12 col-sm-4 tree-container">
				<ul id="departmentTree" class="ztree"></ul>
			</div>
			<div class="col-xs-12 col-sm-8 form-container select-role-form">
				<form action="" method="post" class="form-horizontal">
					<input type="hidden" name="id" id="id" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<div class="col-sm-12">
						<div class="form-group">
						    <label class="col-sm-2 control-label">上级部门</label>
						    <div class="col-sm-10">
						        <span id="parentName"></span>
						        <input id="parentId" name="parent.id" type="hidden"/>
						    </div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="form-group">
						    <label for="inputName" class="col-sm-2 control-label">部门名称</label>
						    <div class="col-sm-10">
						        <input type="text" 
						        	class="form-control" 
						        	id="inputName" 
						        	name="name"
						        	required="required"
						        	placeholder="部门名称"/>
						    </div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="form-group">
						    <label for="selectManager" class="col-sm-2 control-label">部门经理</label>
						    <div class="col-sm-10">
						        <input type="text"
						        	   class="form-control" 
						        	   id="selectManager"/>

						        <input type="hidden"
						        	   id="managerUserId"
						        	   name="manager.user.id"/>
						        
						        
						    </div>
						</div>
					</div>
					<div class="col-sm-12 text-right">
						<button class="btn btn-default reset-button" type="button">重置</button>
						<button class="btn btn-primary">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${ctx }/webjars/devbridge-autocomplete/1.4.8/dist/jquery.autocomplete.min.js"></script> 
<script type="text/javascript" src="${ctx }/js/department.js"></script>
</body>
</html> 