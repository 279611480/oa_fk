<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>登录</title>
</head>
<body>
		<!-- col-md-offset-3 text-center   页面布局 左右隔开3   之前就是重点屏幕6了 再左右都是3那么就是居中了 -->
	<div class="col-sm-12 col-md-6 col-md-offset-3 text-center">
		<form class="form-signin">
			<!-- 头部 -->
			<h2 class="form-signin-heading">请登录</h2>
			<label for="inputLoginName" class="sr-only">请登录</label>	
			<input id="inputLoginName"
				class="form-control"
				placeholder="登录名"
				required="required"
				autofocus="autofocus"/>
				
			<label for="inputPassword" class="sr-only">密码</label>
			<input type="password" 
		    	id="inputPassword" 
		    	class="form-control" 
		    	placeholder="登录密码" 
		    	required="required"/>
		    <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
		</form>
	</div>
</body>
</html> 