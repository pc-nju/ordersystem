//页面加载的时候执行
$(function () {
    // //若有消息的话，加载消息
    common.showMessage($("#message").val());
    // //表单校验
    common.checkBusinessForm();
})
function add() {
	$("#mainForm").submit();
}
function goBack() {
	location.href = $("#basePath").val() + "/businesses";
}
