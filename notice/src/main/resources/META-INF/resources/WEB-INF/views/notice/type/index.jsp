<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <!-- 引入标签库 -->
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

	
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>公告类型管理</title>

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

				//这里  是  将数据显示出来  利用jQuery  调用选择器  设值进去
				$(".role-form [name='id'] ").val(id);
				$(".role-form #inputName").val(name);

			});	
			 
			$(".type").hover(function(){
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
				var url = "${ctx}/notice/type/" + id;
				//发生DELETE请求删除数据
				//DELETE请求，表示删除URL对应的资源，浏览器不能直接发送
				//有两种方式发送：使用AJAX发送、使用POST表单模拟发送（SpringMVC 扩展的，需要一个名为_mothod 的参数，值为DELETE）
				$.ajax({
					url:url,
					method:"DELETE",
					success:function(data,status,xhr){
						//相当于是重定向
						document.location.href="${ctx}/notice/type";
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
			公告类型管理
			<div class="close"><!-- close   意思是 -->
				<a class="btn btn-defaule">新增</a>
			</div>
	    </div>
		<div class="panel-body" >
			<div class="col-sm-12 col-md-4" >
			<!-- 公告类型列表 -->
				<c:forEach items="${types }" var="r"><!-- types是控制层，传过来的（ModelAndView添加进来的） -->
					<div class="col-xs-12 role" 
						data-id="${r.id }"
						data-name="${r.name }"
					>	
					${r.name }
						<span class="pull-right hide remove-btn">X</span>
					</div>
				</c:forEach>
			</div>
			<div class="col-sm-12 col-md-8 role-form">
				<!--公告修改的表单 -->
				<form class="" method="post" class="form-horizontal">
					<input type="hidden" name="id" /><!-- 传入  登录者的id  以方便做修改  但是不想让那个用户看见 -->
					<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
					<div class="col-sm-12 ">
						<div class="form-group">
							<label for="inputName" class="col-sm-2 contro-label" >类型名称</label>
							<div class="col-sm-10">
								<input type="text" 
									class="form-control" 
						        	id="inputName" 
						        	name="name"
						        	required="required"
						        	placeholder="类型名称"/>   	
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