var added = false;// 限制只能每次添加一个菜单
var addHoverDom = function(treeId, treeNode){
	// 找到节点的span
	var sObj = $("#" + treeNode.tId + "_span");
	// 
	if (added // 已经添加一个子菜单，不要再显示添加按钮
			|| treeNode.editNameFlag // 判断是否正在编辑名字
			|| $("#addBtn_" + treeNode.tId).length > 0//判断是否有添加按钮
			){ 
		// 在编辑名字、有添加按钮都不需要再增加新的自定义按钮
		return;
	}
	// 自定义按钮，其实也是一个span
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='添加菜单' onfocus='this.blur();'></span>";
	// 把自定义的按钮的HTML，放到节点的span之后
	sObj.after(addStr);
	// 给按钮绑定事件
	var btn = $("#addBtn_" + treeNode.tId);
	if (btn) btn.bind("click", function(){
		added = true;
		// 删除自定义按钮
		removeHoverDom(treeId, treeNode);
		// 找到已有的tree
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		// treeNode : 添加新的节点到treeNode里面
		// id : 如果为null则自动增加一个、pId : 上级节点的id、name : 显示的名称
		var nodes = zTree.addNodes(treeNode, {name:"新菜单"});
		// 选中节点，因为只是添加了一个，所以nodes里面只有一个！
		zTree.selectNode( nodes[0], false, true);
		
		return false;
	});
};
// treeId表示树的顶层id, node是选中的节点
var showToForm = function(treeId, node){//点击节点（菜单）的时候，就触发此事件，可以将菜单（节点的内容全部显示出来）
	/**
	 * 在点击，菜单的时候，就触发，如果是新增，就清空全部内容的事件，就不用去点击重置按钮了
	 * */
	resultForm;//在选中之前，先要清空表单内容
	
	
	var id = node.id;
	var name = node.name;
	var url = node.url;
	var type = node.type;
	var roles = node.roles;
	$(".form-container #id").val(id);
	$(".form-container #inputName").val(name);
	$(".form-container #inputURL").val(url);
	// name="type"
	// input[name='type'][value='" + type + "']" : 先找到input，然后匹配name属性，并且匹配value属性
	$(".form-container input[name='type'][value='" + type + "']").prop("checked", true);
	//$(".form-container #id").val(id);
	
	// 处理上级菜单
	var parentNode = node.getParentNode();
	if( parentNode ){
		var parentId = parentNode.id;
		var parentName = parentNode.name;
		
		$(".form-container #parentId").val(parentId);
		// span、div可以用text、html函数设置内容
		$(".form-container #parentName").text(parentName);
	}else{
		$(".form-container #parentId").val("");
		$(".form-container #parentName").text("");
	}
	
	
	/* 如果说，选中的节点，里面包含有角色   将它遍历打印出来  
	 * 
	 首先  找到  角色在页面对应位置的（之前在 用户模块  已经实现过 且放在通用JS里面）  存储地方  再遍历打印   */
	if(node.roles){
		$(".remove-all").click();//点击完成后，调用   通用JS的代码  事件   将未选择的全部角色  全部移除
		
		//选中菜单关联的角色
		//jQuery里面调用找到的对象方法，如果全部对象执行相同的操作，不需要一个个来
		//此时直接调用prop相当于会调用每个元素的prop方法
		$(".unselect-roles ul li input").prop("checked", false);
		
		for(var i=0;i<node.roles.length;i++){
			var role = node.roles[i];
			$(".unselect-roles ul li input[value='" + role.id + "']").prop("checked", true);
		}
		
		// 把所有选中的角色添加到【左边的框】里面
		$(".add-selected").click();//点击按钮
	}
};

//定义  删除节点（菜单）函数（方法）
var dropNode = function(event,treeId,treeNodes,targetNode,moveType){
	var requestData = new Object();//new一个要移动的节点对象
	//要移动的节点id  
	requestData.id = treeNodes[0].id;
	if(!requestData.id){//如果没有node(节点)的id，表示新增的节点，不需要到服务器移动
		return;
	}
	
	//要把节点移动到那个位置    targetId 要移到那个位置的Id   
 	if(targetNode){//如果说有目标节点
 		requestData.targetId = targetNode.id;
 	}else{
 		//没有目标节点
 		requestData.targetId = "";
 	}
	// inner prev next    zTree定义的移动三种状态
	requestData.moveType = moveType;
	
	//发送移动的Ajax请求
	$.ajax({
		url:moveURL,
		method:"POST",
		dataType: "json", /* 返回参数数据类型 */
		data:requestData,
		success:function(data,status,xhr){
			//正常的时候什么都不处理
		},
		error:function(data,status,xhr){
			alert(data.responseJSON.message);
		}
		
	});
	
};

//显示删除按钮
var showRemoveBtn = function(treeId,treeNode){
	//如果被选中要删除的菜单，里面，没有其他的下级菜单 ，那么就可以删除，不然的话，先要删除，其他的下级菜单，才能删除，被选中的菜单
	return treeNode.children ==0;
}

//执行删除的操作
var removeNode = function(treeId,treeNode){
	//找到，要删除的节点地方
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	//  false表示不要出发回调
	zTree.removeNode(treeNode,false);
}

var beforeRemoveNode = function(treeId,treeNode){//再删除之前，要做的事情
	//无论如何，都返回false，避免zTree自己把节点删除
	//应该通过Ajax把服务器里面的数据删除成功以后，手动调用API来删除节点
	$.ajax({
		url:removeURL + "/" + treeNode.id,
		method:"DELETE",
		dataType:"json",
		success:function(data,status,xhr){
			// ==  两个等号会进行类型转换，比如 1 == "1"  返回true
			// === 三个等号不会进行类型转换，比如 1 === "1" 返回false
			//在error回调中要加上responseJSON获取服务器返回的JSON
			//在success回调，则不需要responseJSON
			if(data.code === 1){
				//删除成功，把节点从页面删除
				removeNode(treeId,treeNode);
			}
		},
		error:function(data,status,xhr){
			alert(data.responseJSON.message);
		}
		
	});
	
}



var setting = {
	async: {
		// 激活异步请求
		enable: true,
		// 异步请求的URL，默认POST方式发送请求
		url: loadURL,
		// 使用GET方式发送请求
		type: "GET",
		// 要求返回JSON，数据类型参考jQuery的dataType
		dataType: "JSON"
	},
	view: {
		// 当鼠标移动在节点上的时候，增加自定义的按钮
		addHoverDom: addHoverDom,
		// 当鼠标离开节点的时候，删除自定义按钮
		removeHoverDom: removeHoverDom,
		// 禁止多选
		selectedMulti: false
	},
	edit: {
		enable: true,
		// 禁止在菜单树中直接修改名字，而是在点击以后在表单里面来修改
		showRenameBtn: false,
		drag:{
			//拖动的时候禁止copy逻辑会比较简单，在onDrop里面把拖动更新到数据库
			//禁止复制
			isCopy:false,
			//允许拖动
			isMove:true
		},
		showRemoveBtn:showRemoveBtn
	},
	callback: {
		onSelected: showToForm,
		//删除节点  调用dropNode 方法（函数）
		onDrop:dropNode,
		beforeRemove:beforeRemoveNode
		// onRemove: removeNode
	}
};

// 使用var声明的函数，系统在运行的时候会把var放到最上面，但是还未赋值
// 如果使用function直接声明函数，那么会在所有执行语句执行之前，先把函数分配空间
// 内存分配顺序： function -> var -> let
//var removeHoverDom = function(treeId, treeNode){
function removeHoverDom(treeId, treeNode){
	// 删除自定义的按钮
	$("#addBtn_" + treeNode.tId).unbind().remove();
};


/**实现重置方法（函数）*/
var resultForm = function(){
	/**
	 * 因为是新增菜单，所以，表单里面一开始，并不需要任何东西，全部设置为null即可
	 * */
	$(".form-container #id").val("");
	$(".form-container #inputName").val("");
	$(".form-container #inputURL").val("");
	
	$(".form-container input[name='type']").prop("checked", false);

	$(".form-container #parentId").val("");
	$(".form-container #parentName").text("");
	
	/**触发，移除全部角色的功能  新增的时候*/
	$(".remove-all").click();
};

$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting);
	
	$(".result-button").click(resultForm);/**绑定点击重置按钮事件*/
});







