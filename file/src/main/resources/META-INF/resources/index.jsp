<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="yun" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>首页文件</title>
</head>
<body>
		<div class="col-sm-12 col-md-6 col-md-offset-3 text-center">
			<div>
				<form action="${ctx }/file" 
					method="post" 
					enctype="multipart/form-data">
					<input type="file" name="file"/>
					<button type="submit">上传</button> 
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					
				</form>
			</div>
		</div>
</body>
</html> 