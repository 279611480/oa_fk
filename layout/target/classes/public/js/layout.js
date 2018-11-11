/**
 * 在index.jsp页面，加载完后  绑定点击事件  
 */
$(document).ready(function(){
	//显示或者隐藏侧边栏
	$('[data-toggle="sidebar"]').click(function(){
		$('.sidebar').toggleClass('active')//对应index.jsp48行，调用77行  点击事件
	});
	
});




