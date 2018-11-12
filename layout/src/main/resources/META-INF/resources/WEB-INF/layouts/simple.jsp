<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="icon" href="${ctx}/images/favicon.ico"><%-- Logo  名字最好是favicon.ico --%>
    <title>麻花藤智能办公自动化  security模块被统一后，登陆成功的点击security后的页面 --
    		<sitemesh:write property="title"/></title>
    		
    <!-- Bootstrap core CSS -->
	<link rel="stylesheet" href="${ctx }/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"/>	
	<!-- Custom styles for this template -->
	<link href="${ctx }/css/layout.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${ctx }/static/css/yun.css"/>
	     
	<script type="text/javascript" src="${ctx }/webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script>
	
	<!-- 将公共统一的页面(main)的，头部内容使用标签加进来  除了标题以外-->
	<sitemesh:write property="head"/>
  </head>

  <body>
	<div class="container-fluid">
		<sitemesh:write property="body"/>  <!-- 将main.jsp的内容加进来 -->
	</div>
  </body>
</html>
