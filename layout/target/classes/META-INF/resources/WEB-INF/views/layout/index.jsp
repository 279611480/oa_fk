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
    <title>麻花藤智能办公自动化</title>
    <!-- Bootstrap core CSS -->
	<link rel="stylesheet" href="${ctx }/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"/>	
	<!-- Custom styles for this template -->
	<link href="${ctx }/css/layout.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${ctx }/static/css/yun.css"/>
	     
	<script type="text/javascript" src="${ctx }/webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script>

  </head>

  <body>
	<!-- 横幅导航 -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
      
      <!-- 此div是一个导航头部显示的 -->
        <div class="navbar-header">
		<%-- navbar-toggle collapsed : 在正常大小的屏幕中，此按钮是隐藏的，只有在小屏幕的时候才显示出来  34-44 --%>
		<%-- data-target="#navbar"  此按钮显示出来以后，用于【显示/隐藏菜单】 --%>
          <button type="button" 
          class="navbar-toggle collapsed" <%-- 32行 调用46行 的按钮--%>
          data-toggle="collapse" 
          data-target="#navbar" <%-- 33行  调用56行  点击页面按钮显示内容 --%>
          aria-expanded="false" 
          aria-controls="navbar">
          <!-- sr-only  表示用于给屏幕阅读器使用的 -->
            <span class="sr-only">显示或隐藏菜单</span>
            <%-- 对应，小屏幕时的，左边菜单，全被隐藏，那么。按下，此组件，菜单就会显示出来  --%>
           <span class="glyphicon glyphicon-align-justify" style="color: white;"></span>
          </button>
          
          
          <%-- class 32行   data-toggle="sidebar"  调用.css的样式  如 动画帧  与js的点击事件调用77行  --%>
          <button type="button" class="navbar-toggle collapsed" data-toggle="sidebar">
          		<span class="sr-only">显示或隐藏按钮</span>
          		 <%-- 对应，正常屏幕时的，左边菜单--%>
          		 <span class="glyphicon glyphicon-align-justify" style="color: white;"></span>
          </button>
          
          <a class="navbar-brand" href="index.html#">麻花藤智能OA</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="index.html#">首页</a></li>
            <li><a href="index.html#">设置</a></li>
            <li><a href="index.html#">个人</a></li>
            <li><a href="index.html#">帮助</a></li>
            <li><a href="index.html#">退出</a></li>
          </ul>
          <!-- 搜索框可以留着，因为可以让所有的页面使用相同的搜索框	 -->
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
      	<!-- 一级二级显示的地方 -->
        <div class="col-sm-3 col-md-2 sidebar">
	        <ul class="nav nav-sidebar">
		        <li class="active">
		        	<a href="index.html#">
							Overview <span class="sr-only">(current)</span>
					</a>
					</li>
					<li><a href="index.html#">Reports</a></li>
					<li><a href="index.html#">Analytics</a></li>
					<li><a href="index.html#">Export</a></li>
	        </ul>
	        
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				主体内容
		</div>
      </div>
    </div>

   <script type="text/javascript" src="${ctx }/js/layout.js"></script>
  </body>
</html>