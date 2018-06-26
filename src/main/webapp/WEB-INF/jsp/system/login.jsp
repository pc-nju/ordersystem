<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>大众点评后台管理</title>
	<link rel="stylesheet" type="text/css" href="${basePath}/resources/css/login.css" />
	<link rel="stylesheet" type="text/css" href="${basePath}/resources/css/jquery.validate.css" />
	<script src="${basePath}/resources/js/common/jquery-1.8.3.js" type="text/javascript"></script>
	<script src="${basePath}/resources/js/common/jQuery.md5.js" type="text/javascript"></script>
	<script src="${basePath}/resources/js/common/validation/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${basePath}/resources/js/common/validation/messages_zh.js" type="text/javascript"></script>
	<script src="${basePath}/resources/js/common/common.js" type="text/javascript"></script>
	<script src="${basePath}/resources/js/system/login.js" type="text/javascript"></script>
</head>
<body>
<input type="hidden" id="basePath" value="${basePath}"/>
<input type="hidden" id="message" value="${pageCode.msg}"/>
<div class="main">
	<div class="header hide"></div>
	<div class="content">
		<div class="title hide"></div>
		<form id="mainForm" method="post" action="${basePath}/login/validate">
			<fieldset>
				<div class="input">
					<input class="input_all name" name="username" id="username" placeholder="用户名" type="text" onFocus="this.className='input_all name_now';" onBlur="this.className='input_all name'"/>
				</div>
				<div class="input">
					<input type="hidden" name="password" id="password_md5"/>
					<input class="input_all password" id="password" type="password" placeholder="密码" onFocus="this.className='input_all password_now';" onBlur="this.className='input_all password'"/>
				</div>
				<div class="checkbox">
					<input type="checkbox" name="remember" id="remember" /><label for="remember"><span>一周免登陆</span></label>
				</div>
				<div class="enter">
					<input class="button hide" type="button" id="submit_login" onclick="login();" value="登录" />
				</div>
			</fieldset>
		</form>
	</div>
</div>
</body>
</html>