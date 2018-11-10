<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <!-- 引入标签库 -->
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
    <!-- 引入JSP Tag文件 -->
    <%@ taglib prefix="yun" tagdir="/WEB-INF/tags" %>
    
	<!-- 将${pageContext.request.contextPath} 提取出来  免得要用的地方都要打一遍 -->
	<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
	
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>角色管理</title>
	<link rel="stylesheet" href="${ctx }/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"/>
	<script type="text/javascript" src="${ctx }/webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script>
	<!-- 将css样式提取出来 放到resources里面 -->
	<link rel="stylesheet" href="${ctx }/static/css/yun.css"/>
	<style type="text/css">
		
	</style>
	
	<script type="text/javascript">
		 $(function(){
			 /* $(".role").bind("click", function(){
				}); */
			$(".role").click(function(){
				//发生事件的div
				var div = $(this);//div 绑定点击60行 事件
				var id =div.attr("data-id");//将点击后的数据的 内容赋值给id  接收
				var name=div.attr("data-name");
				var roleKey=div.attr("data-roleKey");
				//这里  是  将数据显示出来  利用jQuery  调用选择器  设值进去
				$(".role-form [name='id'] ").val(id);
				$(".role-form #inputName").val(name);
				$(".role-form #inputRoleKey").val(roleKey);
			});	
			 
			$(".role").hover(function(){
				var span = $("span",this);
				//事件发生的时候，会删除hide类
				//鼠标移开以后，会自动加上hide类
				span.toggleClass("hide");
			});	
				
			$(".remove-btn").click(function(event){
				//组织事件的传播
				event.stopPropagation();
				var div = $(this).parent();
				var id = div.attr("data-id");
				var url = "${ctx}/identity/role/"+id;
				//发生DELETE请求删除数据
				//DELETE请求，表示删除URL对应的资源，浏览器不能直接发送
				//有两种方式发送：使用AJAX发送、使用POST表单模拟发送（SpringMVC 扩展的，需要一个名为_mothod 的参数，值为DELETE）
				$.ajax({
					url:url,
					method:"DELETE",
					success:function(data,status,xhr){
						//相当于是重定向
						document.location.href="${ctx}/identity/role";
					},
					error:function(data,status,xhr){
						//responseJSON返回的是JSON对像
						//message是错误信息
						alert(data.responseJSON.message);
					}
					
				});
			});	
				
				
		 });
		
		
	</script>
	
</head>
<body>
<div class="container-fluid">	
	<div class="panel panel-danger">
		<!-- Default panel contents -->
	    <div class="panel-heading">
			角色管理
			<div class="close"><!-- close   意思是 -->
				<a class="btn btn-defaule">新增</a>
			</div>
	    </div>
		<div class="panel-body" >
			<div class="col-sm-12 col-md-4" >
			<!-- 角色列表 -->
				<c:forEach items="${roles }" var="r"><!-- roles是控制层，传过来的（ModelAndView添加进来的） -->
					<div class="col-xs-12 role" 
						data-id="${r.id }"
						data-name="${r.name }"
						data-roleKey="${r.roleKey }"
					>	
						${r.name }(${r.roleKey })	
						<span class="pull-right hide remove-btn">X</span>
					</div>
				</c:forEach>
			</div>
			<div class="col-sm-12 col-md-8 role-form">
				<!-- 角色修改的表单 -->
				<form class="" method="post" class="form-horizontal">
					<input type="hidden" name="id" /><!-- 传入  登录者的id  以方便做修改  但是不想让那个用户看见 -->
					<div class="col-sm-12 ">
						<div class="form-group">
							<label for="inputName" class="col-sm-2 contro-label" >角色名称</label>
							<div class="col-sm-10">
								<input type="text" 
									class="form-control" 
						        	id="inputName" 
						        	name="name"
						        	required="required"
						        	placeholder="角色名称"/>   	
							</div>
						</div>
					</div>
					<div class="col-sm-12 ">
						<div class="form-group">
							<label for="inputRoleKey" class="col-sm-2 contro-label" >角色Key</label>
							<div class="col-sm-10">
								<input type="text" 
									class="form-control" 
						        	id="inputRoleKey" 
						        	name="roleKey"
						        	required="required"
						        	placeholder="用于角色判断的Key，唯一"/>
							</div>
						</div>
					</div>
					<div class="col-xs-12 text-right" >
				    	<button type="submit" class="btn btn-primary">保存</button>
				    </div>
				</form>
			</div>
	 	</div>
	</div>
</div>	
	
	
	
</body>
</html>