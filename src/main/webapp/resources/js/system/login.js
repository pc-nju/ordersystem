$(function () {
	common.showMessage($("#message").val());
	// common.check();
	checkLoginForm();
})
//校验用户名，密码
function checkLoginForm() {
    $("#mainForm").validate({
        rules:{
            "username":{
                required : true,
                rangelength:[2,50]
			},
            "password":{
                required : true,
                rangelength:[8,20]
            }
        }
    });
}

//提交登录信息
function login() {
    //若密码存在
    if ($("#password").val()){
        $("#password_md5").val($.md5($("#password").val()));
    }
    $("#mainForm").submit();
}