<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <!-- 引入标签库 -->
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
	<!-- 将${pageContext.request.contextPath} 提取出来  免得要用的地方都要打一遍 -->
	<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>编辑用户信息</title>
	

	
</head>
<body>
<div class="container-fluid">	
	<div class="panel panel-danger">
		<!-- Default panel contents -->
	    <div class="panel-heading">
	    	编辑用户信息
	    </div>
		<div class="panel-body" >
			<form class="form-horizontal select-role-form" 
				action="${ctx }/identity/user"
				method="post"
				enctype="multipart/form-data">
			
				<!-- 修改用户的时候，需要有id   但是id不想让用户看见 那么，设置为隐藏 -->
				<input name="id" value="${user.id }" type="hidden" />
				
				<!-- 当用户打开表单的时候，生成一个随机验证码存储在表单里面 -->
				<!--  提交的时候，会判断session里面是否有随机验证码，并且要求浏览器提交过来的随机验证码要相同-->
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				
			<div class="col-sm-12 col-md-6">
				<label for="inputName" class="col-sm-2 contro-label" >姓名</label>
				<div class="col-sm-10">
					<input type="text" 
						class="form-control" 
			        	id="inputName" 
			        	name="name"
			        	required="required"
			        	placeholder="用户的真实姓名"
			        	value="${user.name }"
			        	/>   	
				</div>
			</div>
			<div class="col-sm-12 col-md-6">
				<label for="inputLoginName" class="col-sm-2 contro-label" >登录名</label>
				<div class="col-sm-10">
					<input type="text" 
						class="form-control" 
			        	id="inputLoginName" 
			        	name="loginName"
			        	required="required"
			        	<%--  判断用户是否不为空，如果用户不为空，那么在添加页面，只能看到密码，并不能修改，如果用户为空，那么~~随他去吧--%>
			        	${not empty user ? 'readonly="readonly"' : '' }  
			        	placeholder="用于登录系统的账号"
			        	value="${user.loginName }"
			        	/>
					
				</div>
			</div>
				
			<div class="col-sm-12 col-md-6">
				<div class="from-group">
					<label for="inputPassword" class="col-sm-2 contro-label" >密码</label>
						<div class="col-sm-10">
							<input type="password" 
								class="form-control" 
					        	id="inputPassword" 
					        	name="password"
								${empty user ? 'required="required"' : '' }
					        	placeholder="用于登录系统的密码 ${not empty user ? '，不修改则不填写' : '' }"/>
						</div>	
				</div>
			</div>
			
			
				<!--在新增页面  将未选中角色效果添加进来 -->
				<div class="col-sm-12">
					<div class="form-group">
						<label class="col-sm-2 contro-label">角色</label>
						<div class="col-sm-10">
							<div class="col-sm-5">已选择</div>
							<div class="col-sm-2"></div>
							<div class="col-sm-5">未选择</div>
							<div class="row">
								<div class="col-xs-5 roles selected-roles">
									<ul><!-- ul是列表标签  li是它里面的列表项 -->
										<c:forEach items="${user.roles }" var="r"><!-- 将User（对象）的，所选Role（角色）显示出来 -->
											 <c:if test="${not r.fixed }"> 
												<li>
													<label>
														<input type="checkbox" name="roles[0].id" value="${r.id }" />
														${r.name }	
													</label>
												</li>
											 </c:if> 
										</c:forEach>
									</ul>
								</div>
								
								<div class="col-sm-2">
									<div class="btn btn-danger add-selected">添加所选</div>
									<div class="btn btn-danger add-all">添加全部</div>
									<div class="btn btn-danger remove-selected">删除所选</div>
									<div class="btn btn-danger remove-all">删除全部</div>
								</div>
								
								<div class="col-xs-5 roles unselect-roles">
						    		<ul>
						    			<c:forEach items="${roles }" var="r">
						    				<%-- 因为重写了equals和hashCode，才可以使用contains判断r是否在user里面 --%>
						    				 <c:if test="${not user.roles.contains(r) }"> <!-- 去除普通角色 -->
							    				<li>
							    					<label>
							    						<input type="checkbox" name="roles[0].id" value="${r.id }"/>
							    						${r.name }
							    					</label>
							    				</li>
							    			 </c:if> 
						    			</c:forEach>
						    		</ul>
						    	</div>
								
								
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12">
				<div class="form-group">
				    <div class="col-xs-12 text-right">
				    	<button type="submit" class="btn btn-primary">保存</button>
				    </div>
				</div>
			</form>
	 	</div>
	</div>
	<!-- 绑定事件  实现功能 -->
	<script type="text/javascript" src="${ctx }/static/js/yun.js"></script>
	
	
		
</div>	
	
	
	
</body>
</html>