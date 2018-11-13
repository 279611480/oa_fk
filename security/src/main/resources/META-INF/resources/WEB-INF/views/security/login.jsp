<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登录页面</title>
</head>
<body>
		<!-- col-md-offset-3 text-center   页面布局 左右隔开3   之前就是重点屏幕6了 再左右都是3那么就是居中了 -->
	<div class="col-sm-12 col-md-6 col-md-offset-3 text-center">
		<!-- 退出之后，判断URL的login后面是否为空字符串  是的话加上提示信息 -->
		<c:if test="${param.logout eq '' }">
			<div class="alert alert-success" role="alert">成功退出登录</div>
		</c:if>
		
		<!-- security/do-login  在config文件是，调用鉴权模块 这里是使其被鉴权 -->
		<form class="form-signin" action="${ctx }/security/do-login" method="post">
			<!-- 头部 -->
			<h2 class="form-signin-heading">请登录</h2>
			<label for="inputLoginName" class="sr-only">请登录</label>	
			<input id="inputLoginName"
				class="form-control"
				placeholder="登录名"
				required="required"
				autofocus="autofocus"
				name="loginName"/> 
				
			<label for="inputPassword" class="sr-only">密码</label>
			<input type="password" 
		    	id="inputPassword" 
		    	class="form-control" 
		    	placeholder="登录密码" 
		    	required="required"
		    	name="password"/>
		    	<!-- 防跨站攻击 -->
		    	  <input type="hidden"
				name="${_csrf.parameterName}"
				value="${_csrf.token}"/> 
		    	
		    <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
		</form>
	</div>
</body>
</html> 