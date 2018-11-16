<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>编辑公告</title>
</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			编辑公告
		</div>	
		<div class="panel-body">
			表单
			标题、类型选择			
			<div id="noticeContentEditor">
			</div>	
			
			<input type="hidden" name="content" id="noticeContent" />
			按钮，保存的时候，要有状态（草稿、已发布、已撤回），阅读的时候必须是【已发布】的才能阅读。
		</div>
	</div>
</div>

<script type="text/javascript" src="${ctx }/webjars/wangEditor/3.1.1/release/wangEditor.min.js"></script>
<script type="text/javascript">
    var E = window.wangEditor;
    var editor = new E('#noticeContentEditor');
    // 或者 var editor = new E( document.getElementById('editor') );
    
     // 隐藏网络图片引用tab
    //editor.customConfig.showLinkImg = false;
      
     //显示图片上传的Tag
     editor.customConfig.uploadImgServer = '${ctx}/storage/file/wangEditor';  // 上传图片到服务器
    //上传的时候，文件的字段名
    editor.customConfig.uploadFileName = 'file';
    
    // 自定义上传的时候请求头内容
    editor.customConfig.uploadImgHeaders = {
   	    '${_csrf.headerName}': '${_csrf.token}'
   	};
    
    //接收改变后的内容，获取富文本编辑器里面的内容，放到#noticeContent
    editor.customConfig.onchange = function(html){
    	$("#noticeContent").val(html);
    };
    // 忽略粘贴内容中的图片   不忽略改为true即可
    editor.customConfig.pasteIgnoreImg = true
    // 不要过滤复制内容的样式，保持原本的样式，可能有些时候不能完全得到样式，此时可以自定义外观（写CSS）
    editor.customConfig.pasteFilterStyle = false;
    
    // 创建编辑器
    editor.create();
</script>


</body>
</html>