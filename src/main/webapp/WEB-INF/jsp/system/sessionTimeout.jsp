<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>session超时</title>
	<script src="${basePath}/resources/js/common/common.js" type="text/javascript"></script>
	<script type="text/javascript">
		common.showMessage('${pageCode.msg}');
		//当前窗口
		var topWindow = window;
		//若当前窗口的父窗口不是自己，说明当前窗口不是最外围窗口
		while (topWindow.parent !== topWindow){
		    topWindow = topWindow.parent;
		}
		//跳转到登录页面
		topWindow.location.href = "${basePath}/login";
	</script>
</head>
<body>

</body>
</html>